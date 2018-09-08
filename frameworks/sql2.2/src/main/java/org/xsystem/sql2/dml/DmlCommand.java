/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.dml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.io.IOUtils;
import org.xsystem.sql2.convertor.Converter;
import org.xsystem.sql2.convertor.DefaultConvertor;
import org.xsystem.sql2.convertor.JdbcTypeMapping;
import org.xsystem.sql2.dml.jdbcnative.AbstactNativeHelper;
import org.xsystem.sql2.dml.jdbcnative.DumeNativeHelper;
import org.xsystem.sql2.dml.jdbcnative.OracleNativeHalper;
import org.xsystem.sql2.dml.jdbcnative.PostgreSQLNativeHalper;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author atimofeev
 */
public class DmlCommand {

    private static final Converter converter = new DefaultConvertor();
    Boolean upperTag;
    boolean namedParams = false;

    DmlCancel dog = null;

    public DmlCommand() {
    }

    public void setDog(DmlCancel dog) {
        this.dog = dog;
    }

    public void setUpperTag(Boolean upperTag) {
        this.upperTag = upperTag;
    }

    public void setNamedParams(boolean namedParams) {
        this.namedParams = namedParams;
    }

    public Object convert(Object value, int jdbcType) {
        Class claz = JdbcTypeMapping.translateToJava(jdbcType);
        if (claz != null) {
            Object ret = converter.convert(value, claz);
            return ret;
        }
        return value;
    }

    void initDog(Statement stmt) {
        if (dog != null) {
            dog.setStatement(stmt);
        }
    }

    public void batch(Connection con, String stmt, List<DmlParams> paramsSpec, List<Map<String, Object>> valuesList) {
        PreparedStatement cs = null;//connection.prepareStatement(sql);

        try {
            cs = con.prepareStatement(stmt);
            initDog(cs);

            for (Map<String, Object> values : valuesList) {
                setParms(cs, paramsSpec, values);
                cs.addBatch();
            }
            cs.executeBatch();

        } catch (SQLException ex) {
            buildError(ex, stmt, paramsSpec, null);
        } finally {
            initDog(null);
            Auxilary.close(cs);
        }

    }

    public Object execute(Connection con, String stmt, List<DmlParams> paramsSpec, Map<String, Object> values, boolean singleRow) {

        Object ret = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = con.prepareCall(stmt);
            initDog(cs);
            setParms(cs, paramsSpec, values);
            if (cs.execute()) {
                rs = cs.getResultSet();
                ret = new ArrayList();
                Map row = null;
                while (rs.next()) {
                    row = rowAsMap(rs);
                    if (singleRow) {
                        break;
                    }
                    ((List) ret).add(row);
                }
                if (singleRow) {
                    return row;
                };
                return ret;
            } else {
                ret = buildOutParams(cs, paramsSpec);
                if (ret == null) {
                    ret = (Integer) cs.getUpdateCount();
                }
            }
        } catch (SQLException ex) {
            buildError(ex, stmt, paramsSpec, values);
        } finally {
            initDog(cs);
            Auxilary.close(rs);
            Auxilary.close(cs);
        }
        return ret;
    }

    public Stream<Map<String, Object>> stream(Connection con, String stmt, List<DmlParams> paramsSpec, Long skip, Integer total, Map<String, Object> value) {
        ResultSetIterator rsItr = new ResultSetIterator(con, stmt, paramsSpec, skip, total, value,dog);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(rsItr, 0), false);
    }

    public Map<String, Object> row(Connection con, String stmt, List<DmlParams> paramsSpec, Map<String, Object> value) {
        ResultSetIterator rsItr = new ResultSetIterator(con, stmt, paramsSpec, null, null, value,dog);
        if (rsItr.hasNext()) {
            Map<String, Object> ret = rsItr.next();
            rsItr.close();
            return ret;
        }
        return null;
    }

    // ----------------- IMPLEMENTATION-----------------------------------------
    Blob setBlob(Connection con, byte[] data) throws SQLException {
        Blob myBlob = con.createBlob();
        OutputStream os = myBlob.setBinaryStream(0);
        try {
            IOUtils.write(data, os);
            os.flush();
            return myBlob;
        } catch (IOException ex) {
            throw new SQLException(ex);
        } finally {
            Auxilary.close(os);
        }
    }

    Clob setClob(Connection con, String data) throws SQLException {
        Clob myClob = con.createClob();
        Writer wr = myClob.setCharacterStream(0);
        try {
            wr.write(data);
            wr.flush();
            return myClob;
        } catch (IOException ex) {
            throw new SQLException(ex);
        } finally {
            Auxilary.close(wr);
        }

    }

    byte[] getBlob(Blob myBlob) throws SQLException {
        InputStream is = myBlob.getBinaryStream();
        try {
            byte[] ret = IOUtils.toByteArray(is);
            return ret;
        } catch (IOException ex) {
            throw new SQLException(ex);
        } finally {
            Auxilary.close(is);
        }

    }

    String getClob(Clob clob) throws SQLException {
        Reader rdr = clob.getCharacterStream();
        try {
            String ret = IOUtils.toString(rdr);
            return ret;
        } catch (IOException ex) {
            throw new SQLException(ex);
        } finally {
            Auxilary.close(rdr);
        }
    }

    Object setValue(Object value, int jdbcType, String objectType, Connection con, AbstactNativeHelper nativeHelper) throws SQLException {
        if (jdbcType == Types.ARRAY && value != null) {
            List array = (List) value;
            value = nativeHelper.createNamedArray(con, objectType, array);

        } else if (jdbcType == Types.STRUCT && value != null) {
            Map structValue = (Map) value;
            value = nativeHelper.createStructure(con, objectType, structValue);
        } else if (jdbcType == Types.BLOB && value instanceof byte[]) {
            byte[] data = (byte[]) value;
            value = setBlob(con, data);

        } else if (jdbcType == Types.CLOB && value instanceof String) {
            String data = (String) value;
            value = setClob(con, data);

        }
        return value;
    }

    Object getValue(Object retValue, int jdbcType, String objectType, Connection con, AbstactNativeHelper nativeHelper) throws SQLException {
        if (jdbcType == Types.ARRAY && retValue != null) {
            Array array = (Array) retValue;
            retValue = nativeHelper.createList(con, array, objectType);
        } else if (jdbcType == Types.STRUCT && retValue != null) {
            Struct structValue = (Struct) retValue;
            retValue = nativeHelper.createMap(con, structValue, objectType);
        } else if (jdbcType == Types.BLOB && retValue != null) {
            Blob blob = (Blob) retValue;
            retValue = getBlob(blob);
        } else if (jdbcType == Types.CLOB && retValue != null) {
            Clob clob = (Clob) retValue;
            retValue = getClob(clob);
        } else if (jdbcType == Types.OTHER && retValue != null) {
            retValue = nativeHelper.getOTHER(con, retValue);
        }
        return retValue;
    }

    void setParms(CallableStatement cs, List<DmlParams> paramsSpec, Map<String, Object> values) throws SQLException {
        int i = 0;
        Connection con = cs.getConnection();
        AbstactNativeHelper nativeHelper = nativeHelperFactory(con);
        for (DmlParams row : paramsSpec) {
            String paramName = row.getName();
            Integer jdbcType = row.getJdbcType();
            String objectType = row.getObjectType();
            Object value = values.get(paramName);
            boolean in = row.isIn();
            boolean out = row.isOut();
            i = i + 1;
            if (in) {
                if (namedParams) {
                    if (jdbcType == Types.OTHER) {
                        cs.setObject(paramName, value);
                    } else {
                        value = setValue(value, jdbcType, objectType, con, nativeHelper);
                        if (value != null) {
                            value = convert(value, jdbcType);
                            cs.setObject(paramName, value, jdbcType);
                        } else {
                            cs.setNull(paramName, jdbcType);
                        }
                    }
                } else {
                    if (jdbcType == Types.OTHER) {
                        cs.setObject(i, value);
                    } else {
                        if (jdbcType == JdbcTypeMapping.json) {
                            nativeHelper.setJSONPARAM(cs, i, value);
                        } else {
                            value = setValue(value, jdbcType, objectType, con, nativeHelper);

                            if (value != null) {
                                value = convert(value, jdbcType);
                                cs.setObject(i, value, jdbcType);
                            } else {
                                cs.setNull(i, jdbcType);
                            }
                        }
                    }
                }
            }
            if (out) {
                if (namedParams) {

                    if (jdbcType == Types.ARRAY || jdbcType == Types.STRUCT) {
                        cs.registerOutParameter(paramName, jdbcType, objectType);
                    } else {
                        cs.registerOutParameter(paramName, jdbcType);
                    }

                } else {
                    if (jdbcType == Types.ARRAY || jdbcType == Types.STRUCT) {
                        cs.registerOutParameter(i, jdbcType, objectType);
                    } else {
                        cs.registerOutParameter(i, jdbcType);
                    }
                }
            }
        }
    }

    void setParms(PreparedStatement ps, List<DmlParams> paramsSpec, Map<String, Object> values) throws SQLException {
        int i = 0;
        Connection con = ps.getConnection();
        AbstactNativeHelper nativeHelper = nativeHelperFactory(con);
        for (DmlParams row : paramsSpec) {
            String paramName = row.getName();
            Integer jdbcType = row.getJdbcType();
            String objectType = row.getObjectType();
            Object value = values.get(paramName);
            boolean in = row.isIn();
            boolean out = row.isOut();
            i = i + 1;
            if (in) {

                if (jdbcType == Types.OTHER) {
                    ps.setObject(i, value);
                } else {
                    if (jdbcType == JdbcTypeMapping.json) {
                        nativeHelper.setJSONPARAM(ps, i, value);
                    } else {
                        value = setValue(value, jdbcType, objectType, con, nativeHelper);

                        if (value != null) {
                            value = convert(value, jdbcType);
                            ps.setObject(i, value, jdbcType);
                        } else {
                            ps.setNull(i, jdbcType);
                        }
                    }
                }
            }
        }
    }

    Map buildOutParams(CallableStatement cs, List<DmlParams> paramsSpec) throws SQLException {
        Map ret = null;
        Connection con = cs.getConnection();
        AbstactNativeHelper nativeHelper = nativeHelperFactory(con);
        int i = 0;
        for (DmlParams row : paramsSpec) {
            String paramName = row.getName();
            Integer jdbcType = row.getJdbcType();
            String objectType = row.getObjectType();
            boolean out = row.isOut();
            i = i + 1;
            if (out) {
                if (ret == null) {
                    ret = new LinkedHashMap();
                }
                if (namedParams) {
                    Object retValue = cs.getObject(paramName);
                    retValue = getValue(retValue, jdbcType, objectType, con, nativeHelper);

                    ret.put(paramName, retValue);
                } else {
                    Object retValue = cs.getObject(i);
                    if (Auxilary.isEmptyOrNull(paramName)) {
                        paramName = "" + i;
                    }
                    retValue = getValue(retValue, jdbcType, objectType, con, nativeHelper);
                    ret.put(paramName, retValue);
                }
            }
        }
        return ret;
    }

    Map<String, Object> rowAsMap(ResultSet rs) throws SQLException {
        Map row = new LinkedHashMap();

        Connection con = rs.getStatement().getConnection();
        AbstactNativeHelper nativeHelper = nativeHelperFactory(con);
        ResultSetMetaData metaData = rs.getMetaData();
        int cnt = metaData.getColumnCount();
        for (int i = 1; i <= cnt; i++) {
            String cn = metaData.getColumnName(i);
            int jdbcType = metaData.getColumnType(i);
            String colTypeName = metaData.getColumnTypeName(i);
            Object value = rs.getObject(i);
            if (rs.wasNull()) {
                value = null;
            }
            if (upperTag != null) {
                if (upperTag) {
                    cn = cn.toUpperCase();
                } else {
                    cn = cn.toLowerCase();
                }
            }
            value = getValue(value, jdbcType, colTypeName, con, nativeHelper);
            row.put(cn, value);
        }

        return row;
    }

    void buildError(SQLException e, String sql, List<DmlParams> paramsSpec, Map<String, Object> values) {
        DataAccessException ex = new DataAccessException(e);
        String ret = "state -" + ex.state + " ";
        ret = ret + "SQL -" + sql.trim() + " ";
        if (values != null) {
            ret = ret + "PARAMS -[";
            int i = 0;
            for (DmlParams row : paramsSpec) {
                i = i + 1;
                String paramName = row.getName();
                Object val = values.get(paramName);
                if (namedParams) {
                    ret = ret + "" + paramName + "~" + val + " ";
                } else {
                    ret = ret + "" + i + "~" + val + " ";
                }
            }
            ret = ret + "]";
        }
        ex.state = ret;
        throw ex;
    }

    public AbstactNativeHelper nativeHelperFactory(Connection connection) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        String dbname = meta.getDatabaseProductName();
        if (dbname.equals("Oracle")) {
            return new OracleNativeHalper(this);
        } else if (dbname.equals("PostgreSQL")) {
            return new PostgreSQLNativeHalper(this);
        } else {
            return new DumeNativeHelper(this);
        }
    }

    public Map<String, Object> resultSetMetaData(Connection con, String stmt, List<DmlParams> paramsSpec, Map<String, Object> values) {
        Map row = new LinkedHashMap();
        ResultSet rs = null;
        CallableStatement cs = null;
        try {
            cs = con.prepareCall(stmt);
            setParms(cs, paramsSpec, values);
            if (cs.execute()) {
                rs = cs.getResultSet();
                ResultSetMetaData rsmd = rs.getMetaData();
                int cols = rsmd.getColumnCount();

                for (int i = 1; i <= cols; i++) {
                    String colName = rsmd.getColumnName(i);
                    int colType = rsmd.getColumnType(i);
                    row.put(colName, colType);
                }
            };
        } catch (SQLException ex) {
            buildError(ex, stmt, paramsSpec, values);

        } finally {
            Auxilary.close(rs);
            Auxilary.close(cs);
        }
        return row;
    }

    class ResultSetIterator implements Iterator<Map<String, Object>> {

        private ResultSet rs;
        private CallableStatement cs;
        Connection connection;
        List<DmlParams> paramsSpec;
        Long skip;
        Integer total;
        Map<String, Object> values;
        String stmt;
        DmlCancel dog;
        private int count;

        void initDog(Statement stmt) {
            if (dog != null) {
                dog.setStatement(stmt);
            }
        }

        public ResultSetIterator(Connection con, String stmt, List<DmlParams> paramsSpec, Long skip, Integer total, Map<String, Object> values, DmlCancel dog) {
            this.connection = con;
            this.stmt = stmt;
            this.paramsSpec = paramsSpec;
            this.skip = skip;
            this.total = total;
            this.values = values;
            this.dog = dog;
        }

        public void init() {
            try {
                cs = connection.prepareCall(stmt);
                initDog(cs);
                setParms(cs, paramsSpec, values);

                rs = cs.executeQuery();
                if (skip != null) {
                    int i = 0;
                    while (i < skip) {
                        boolean hasRecord = rs.next();
                        if (!hasRecord) {
                            break;
                        }
                        i = i + 1;
                    }
                }
                count = 0;
            } catch (SQLException e) {
                initDog(null);
                close();
                buildError(e, stmt, paramsSpec, values);
            }
        }

        @Override
        public boolean hasNext() {

            if (cs == null) {
                init();
            }
            try {
                boolean hasMore = rs.next();

                if (total != null) {
                    if (count >= total) {
                        hasMore = false;
                    }
                    count = count + 1;
                }
                if (!hasMore) {
                    initDog(null);
                    close();
                }
                return hasMore;
            } catch (SQLException e) {
                initDog(null);
                close();
                throw new DataAccessException(e);
            }

        }

        @Override
        public Map<String, Object> next() {
            try {
                return rowAsMap(rs);
            } catch (SQLException e) {
                close();
                throw new DataAccessException(e);
            }
        }

        public void close() {
            Auxilary.close(rs);
            Auxilary.close(cs);
        }
    }
}
