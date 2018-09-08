/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.page.functions;

import java.sql.Connection;
import java.util.Map;
import org.xsystem.sql2.page.SQL;

/**
 *
 * @author atimofeev
 */
public class SqlFunctions {
    final Connection connection;

    public  Object[] arglist(Object... args){
        if (args==null){
          Object[] ret=new Object[1];
          ret[0]=null;
          return ret;
        }
        return args; 
    }
            
    public SqlFunctions(Connection connection) {
        this.connection = connection;
    }
    
    public Object exec(String sql, Object... values) {
        Object ret=SQL.exec(connection, sql, SQL.arglist(values),false);
        return ret;    
    }
    
    
    
    public Object row(String sql) {
        Object ret=SQL.exec(connection, sql, SQL.arglist(),true);
        return ret;
    }
    
    public Object row(String sql, Object[] values) {
        Object ret=SQL.exec(connection, sql, values,true);
        return ret;
    }
    
    public Object rowValue(String sql,String fld) {
        Object ret=SQL.exec(connection, sql, SQL.arglist(),true);
        if (ret!=null){
           Map m=(Map) ret;
           return m.get(fld);
        }
        return null;
    }
    
    public Object rowValue(String sql,String fld, Object[] values) {
        Object ret=SQL.exec(connection, sql, values,true);
        if (ret!=null){
           Map m=(Map) ret;
           return m.get(fld);
        }
        return null;
    }
}
