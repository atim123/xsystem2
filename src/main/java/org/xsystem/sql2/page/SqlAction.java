/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.page;

import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.xsystem.sql2.convertor.JdbcTypeMapping;
import org.xsystem.sql2.dml.DataAccessException;
import org.xsystem.sql2.dml.DmlCancel;
import org.xsystem.sql2.dml.DmlCommand;
import org.xsystem.sql2.dml.DmlParams;
import org.xsystem.sql2.page.mvel.PageMVELFactory;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author atimofeev
 */
public abstract class SqlAction {

    private final List<ContextDefinition> definitions = new ArrayList();

    private final List<ContextDefinition> params = new ArrayList();

    private String elsql;
    private boolean namedparameters = false;
    private Boolean upperTag;
    private Boolean singleRow = true;
    private String skipEval = null;
    private String totalEval = null;

    public String getSkip() {
        return skipEval;
    }

    public void setSkip(String skip) {
        this.skipEval = skip;
    }

    public String getElSql() {
        return elsql.trim();
    }

    public String getTotal() {
        return totalEval;
    }

    public void setTotal(String total) {
        this.totalEval = total;
    }

    public Boolean getSingleRow() {
        return singleRow;
    }

    public void setSingleRow(Boolean singleRow) {
        this.singleRow = singleRow;
    }

    public Boolean getUpperTag() {
        return upperTag;
    }

    public void setUpperTag(Boolean upperTag) {
        this.upperTag = upperTag;
    }

    public abstract Object exec(Connection connection, Map<String, Object> params, DmlCancel dog);

    public abstract void batch(Connection connection, List<Map<String, Object>> params,DmlCancel dog);

    public abstract Stream<Map<String, Object>> execQuery(Connection connection, Long skip, Integer total, Map<String, Object> params,DmlCancel dog);

    public String getSql() {
        return elsql;
    }

    public void setSql(String sql) {
        this.elsql = sql.trim();
    }

    public void setNamedparameters(boolean namedparameters) {
        this.namedparameters = namedparameters;
    }

    public boolean isNamedparameters() {
        return namedparameters;
    }

    public List<ContextDefinition> getDefinitions() {
        return definitions;
    }

    public List<ContextDefinition> getParams() {
        return params;
    }

    String buildSql(PageMVELFactory mvel) {
        String sql = mvel.resolveTemplate(elsql);

        sql = sql.trim();
        return sql;
    }

    Long buidSkip(PageMVELFactory mvel, Map<String, Object> context) {
        String elSkip = this.getSkip();
        if (!Auxilary.isEmptyOrNull(elSkip)) {
            Long ret = mvel.getValue(elSkip, Long.class);
            return ret;
        }
        return null;
    }

    Integer buidTotal(PageMVELFactory mvel, Map<String, Object> context) {
        String elTotatal = this.getTotal();
        if (!Auxilary.isEmptyOrNull(elTotatal)) {
            Integer ret = mvel.getValue(elTotatal, Integer.class);
            return ret;
        }
        return null;
    }

    void buildDefinitions(PageMVELFactory mvel, Map<String, Object> context) {
        definitions.forEach((ContextDefinition definition) -> {
            String name = definition.getName();
            String ifEl = definition.getIfExpr();
            String elvalue = definition.getValue();
            boolean set = true;
            if (!Auxilary.isEmptyOrNull(ifEl)) {
                set = mvel.getValue(ifEl, Boolean.class);
            }
            if (set) {
                Object value = mvel.getValue(elvalue);
                mvel.setObject(name, value);
            }
        });
    }

    public List<DmlParams> buildBatchParams(){
        List<DmlParams> ret = new ArrayList();
        params.stream().forEach((paramDefinition) -> {
           String type = paramDefinition.getType();
                String name = paramDefinition.getName();
                String objectType = paramDefinition.getObjectType();

                boolean isIn = paramDefinition.isIn();
                boolean isOut = paramDefinition.isOut();
                int jdbcType = JdbcTypeMapping.JDBCTypeTranslate(type);
                DmlParams dmlParams = new DmlParams();
                dmlParams.setName(name);
                dmlParams.setJdbcType(jdbcType);
                dmlParams.setObjectType(objectType);
                dmlParams.setIn(true);
               // dmlParams.setOut(isOut);
                ret.add(dmlParams); 
        });
        return ret;        
    }
    
    public List<DmlParams> buildDmlParams(PageMVELFactory mvel) {
        List<DmlParams> ret = new ArrayList();
        params.stream().forEach((paramDefinition) -> {
            String ifEl = paramDefinition.getIfExpr();
            boolean set = true;
            if (!Auxilary.isEmptyOrNull(ifEl)) {
                set = mvel.getValue(ifEl, Boolean.class);
            }
            if (set) {
                String type = paramDefinition.getType();
                String name = paramDefinition.getName();
                String objectType = paramDefinition.getObjectType();

                boolean isIn = paramDefinition.isIn();
                boolean isOut = paramDefinition.isOut();
                int jdbcType = JdbcTypeMapping.JDBCTypeTranslate(type);
                DmlParams dmlParams = new DmlParams();
                dmlParams.setName(name);
                dmlParams.setJdbcType(jdbcType);
                dmlParams.setObjectType(objectType);
                dmlParams.setIn(isIn);
                dmlParams.setOut(isOut);
                ret.add(dmlParams);
            }
        });
        return ret;
    }

    public Map<String, Object> buildParams(PageMVELFactory mvel, Map<String, Object> prms) {
        Map ret = new LinkedHashMap();
        params.stream().forEach((paramDefinition) -> {
            boolean isIn = paramDefinition.isIn();
            if (isIn) {
                String ifEl = paramDefinition.getIfExpr();
                boolean set = true;
                if (!Auxilary.isEmptyOrNull(ifEl)) {
                    set = mvel.getValue(ifEl, Boolean.class);
                }

                if (set) {
                    String paramName = paramDefinition.getName();
                    String elValue = paramDefinition.getValue();
                    if (elValue != null) {
                        Object value = mvel.getValue(elValue);
                        ret.put(paramName, value);
                    } else {
                        Object value = prms.get(paramName);
                        ret.put(paramName, value);
                    }
                }
            }
        });
        return ret;
    }

}
/*
 
 //--------------------------JDBC 4.2 -----------------------------

 public static final int TIMESTAMP_WITH_TIMEZONE = 2014;

 // Prevent instantiation


 STRING 











 String.class string 

 String.class CHAR 

 String.class  

 char String

 .class VARCHAR 

 String.class varchar 

 String.class NUMERIC 

 java.math.BigDecimal.class numeric 

 java.math.BigDecimal.class BIT 

 Boolean.class bit 

 Boolean.class BOOLEAN 

 Boolean.class  

 boolean Boolean

 .class TINYINT 

 Byte.class tinyint 

 Byte.class SMALLINT 

 Short.class smalint 

 Short.class INTEGER 

 Integer.class integer 

 Integer.class BIGINT 

 Long.class bigint 

 Long.class REAL 

 Float.class real 

 Float.class FLOAT 

 Double.class  

 float Double

 .class DOUBLE 

 Double.class  

 double Double

 .class BINARY 

 byte[].

 class binary 

 byte[].

 class VARBINARY 

 byte[].

 class varbinary 

 byte[].

 class LONGVARBINARY 

 byte[].

 class longvarbinary 

 byte[].

 class DATE 

 java.sql.Date.class date 

 java.sql.Date.class TIME 

 java.sql.Time.class time 

 java.sql.Time.class TIMESTAMP 

 java.sql.Timestamp.class timestamp 

 java.sql.Timestamp.class  
                                                                                                                                                    
                                                                                                                                                
                                                                                                                                            
                                                                                                                                        
                                                                                                                                    
                                                                                                                                
                                                                                                                            
                                                                                                                        
                                                                                                                    
                                                                                                                
                                                                                                            
                                                                                                        
                                                                                                    
                                                                                                
                                                                                            
                                                                                        
                                                                                    
                                                                                
                                                                            
                                                                        
                                                                    
                                                                
                                                            
                                                        
                                                    
                                                
                                            
                                        
                                    
                                
                            
                        
                    
                
            
        
    

 /
 */
