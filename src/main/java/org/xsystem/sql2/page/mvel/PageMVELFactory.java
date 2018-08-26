/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.page.mvel;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.mvel2.MVEL;
import org.mvel2.templates.TemplateRuntime;
import org.xsystem.sql2.page.functions.PageFunctions;
import org.xsystem.sql2.page.functions.SqlFunctions;

/**
 *
 * @author atimofeev
 */
public class PageMVELFactory {
   Map actionContext=new LinkedHashMap();
    Connection connection;
    
    public PageMVELFactory(Map actionContext, Connection connection){
        this.connection = connection;
        this.actionContext.putAll(actionContext);
        SqlFunctions sqlFunctions = new SqlFunctions(connection);
        PageFunctions pageFunction = new PageFunctions(actionContext);
        this.actionContext.put("SQL",sqlFunctions);
        this.actionContext.put("PAGE",pageFunction);
       
    }
    
    public void setObject(String name,Object value){
        actionContext.put(name, value);
    }
    
    public Object getValue(String mvel) {
      Object ret=   MVEL.eval(mvel,actionContext);
      return ret;
    }
    
    public <T> T getValue(String mvel,Class<T> toType) {
      Object ret=   MVEL.eval(mvel,actionContext,toType);
      return(T) ret;
    }
    
    public String resolveTemplate(String template){
       String ret= (String)TemplateRuntime.eval(template,actionContext);
       return ret;
    } 
}
