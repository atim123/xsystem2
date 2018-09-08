/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.page;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.sql.Types;
import org.xsystem.sql2.dml.DmlCommand;
import org.xsystem.sql2.dml.DmlParams;

/**
 *
 * @author atimofeev
 */
public class SQL {
    public static Object[] arglist(Object... args) {
        return args;
    }
    
     public static Object exec(Connection connection, String sql){
        Object ret= exec(connection,sql,arglist());
        return ret;
     }
    
     public static Object exec(Connection connection, String sql, Object[] params){
         return exec(connection,sql,params,true);
     }
     
     public static List list(Connection connection, String sql, Object[] params){
        Object ret= exec(connection,sql,params,false);
        return (List)ret;
     }
     
     public static List list(Connection connection, String sql){
        return list(connection,sql,arglist());
    }
    
     public static Map row(Connection connection, String sql){
         List lst=list(connection,sql);
         if (lst.isEmpty()){
             return null;
         }
         Map ret=(Map)lst.get(0);
         return ret;
     } 
     
    public static Object exec(Connection connection, String sql, Object[] params,boolean singleRow){
       DmlCommand dmlCommand=new DmlCommand();
       List<DmlParams> paramsSpec = new ArrayList();
       Map paramsValue=new LinkedHashMap();
       for (int i=0;i<params.length;i++){
           String paramName="p"+i;
           DmlParams dmlParams=new DmlParams();
           dmlParams.setIn(true);
           dmlParams.setName(paramName);
           dmlParams.setJdbcType(Types.OTHER);
           paramsValue.put(paramName,params[i]);
           paramsSpec.add(dmlParams);
       }
       
       Object ret=dmlCommand.execute(connection,sql,paramsSpec,paramsValue,singleRow);
       return ret;
    }
}
// boolean singleRow