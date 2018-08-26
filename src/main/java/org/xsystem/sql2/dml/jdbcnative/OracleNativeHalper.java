/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.dml.jdbcnative;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.jdbc.OracleConnection;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.xsystem.sql2.dml.DmlCommand;

/**
 *
 * @author atimofeev
 */
public class OracleNativeHalper extends AbstactNativeHelper {

    public OracleNativeHalper(DmlCommand dmlCommand) {
        super(dmlCommand);
    }

    @Override
    public  Object getOTHER(Connection con,Object retType){
        return retType;
    }
    
    public  void    setJSONPARAM(PreparedStatement ps,int pram,Object value) {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates. 
    }
    
    @Override
    public Struct createStructure(Connection con, String baseTypeName, Map elements) throws SQLException {
        Map attrs = new LinkedHashMap();
        OracleConnection nativeconnection = con.unwrap(OracleConnection.class);
        StructDescriptor structdesc = StructDescriptor.createDescriptor(baseTypeName, nativeconnection);
        ResultSetMetaData meta = structdesc.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String colName = meta.getColumnName(i);
            int jdbcType = meta.getColumnType(i);
            String colType = meta.getColumnTypeName(i);
            //   System.out.println(colName + " " + jdbcType + " " + colType);        
            Object value = elements.get(colName);///EMPL EMPL
            if (jdbcType == Types.ARRAY) {
                List params = (List) value;
                value = createNamedArray(con, colType, params);
            } else if (jdbcType == Types.STRUCT) {
                Map params = (Map) value;
                value = createStructure(con, colType, params);
            }
            attrs.put(colName, value);
        }
        STRUCT str = new STRUCT(structdesc, nativeconnection, attrs);
        return str;

    }

    @Override
    public Map createMap(Connection con, Struct struct, String baseTypeName) throws SQLException {
        Map ret=new LinkedHashMap();
      STRUCT nativestr=(STRUCT) struct; 
      StructDescriptor descr=nativestr.getDescriptor();
      Object[] content=nativestr.getAttributes();
      ResultSetMetaData meta=descr.getMetaData();
      for (int i = 1; i <= meta.getColumnCount(); i++) {
            String colName = meta.getColumnName(i);
            int jdbcType = meta.getColumnType(i);
            String colType = meta.getColumnTypeName(i);
            Object value=null;
            if (i-1<content.length){
                value=content[i-1];
            }
            if (jdbcType == Types.ARRAY&&value!=null) {
               Array arrayValue=(Array) value;
               value=createList(con,arrayValue,colType);
            } else if (jdbcType == Types.STRUCT&&value!=null){
               Struct structValue=(Struct) value;
               value = createMap(con,structValue,colType); 
            }
            ret.put(colName, value);
      }      
      return ret;
    }

    @Override
    public List createList(Connection con, Array array, String baseTypeNameP) throws SQLException {
        List ret = new ArrayList();
        int jdbctype = array.getBaseType();
        String baseTypeName = array.getBaseTypeName();
        Object obj = array.getArray();
        Object[] content = (Object[]) obj;
        for (int i = 0; i < content.length; i++) {
            Object value=content[i];
            if (jdbctype == Types.ARRAY&&value!=null) {
               Array arrayValue=(Array) value;
               value=createList(con,arrayValue,baseTypeName);
            } else if (jdbctype == Types.STRUCT&&value!=null){
                Struct structValue=(Struct) value;
                value = createMap(con,structValue,baseTypeName);
            }
            ret.add(value);
        }
        return ret;
    }
    
    @Override
    public Array createNamedArray(Connection con, String baseTypeName, List elements) throws SQLException {
        Object[] attrs = new Object[elements.size()];
        OracleConnection nativeconnection = con.unwrap(OracleConnection.class);
        ArrayDescriptor arraydesc = ArrayDescriptor.createDescriptor(baseTypeName, nativeconnection);
        int jdbcType = arraydesc.getBaseType();
        String typeName = arraydesc.getBaseName();
        for (int i = 0; i < attrs.length; i++) {
            Object value = elements.get(i);
            if (jdbcType == Types.ARRAY) {
                List params = (List) value;
                value = createNamedArray(con, typeName, params);
            } else if (jdbcType == Types.STRUCT) {
                Map params = (Map) value;
                value = createStructure(con, typeName, params);
            }
            attrs[i] = value;
        }
        Array ret = nativeconnection.createOracleArray(baseTypeName, attrs);
        return ret;
    }

}
