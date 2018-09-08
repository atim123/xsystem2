/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.page;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.w3c.dom.Document;

/**
 *
 * @author atimofeev
 */
public interface PageHelper {
    public Connection getConnection();
    public Document   getDocument();
    
    public default Object dml(String path, Map<String, Object> params) {
        Connection con=getConnection();
        Document   xml=getDocument();
        Object ret= PAGE.dml(con, xml,path,params);
        return ret;
    }
    
    public default Map<String, Object> row(String path, Map<String, Object> params) {
        Connection con=getConnection();
        Document   xml=getDocument();
        Map<String, Object> ret= PAGE.row(con, xml,path,params);
        return ret;
    }
    
   public default Object rowValue(String path, Map<String, Object> params,String rowName) {
        Connection con=getConnection();
        Document   xml=getDocument();
        Object  ret= PAGE.rowValue(con, xml,path,params,rowName);
        return ret;
    }
  
   public default Stream<Map<String, Object>> stream(String path, Map<String, Object> params){
       Connection con=getConnection();
        Document   xml=getDocument();
        Stream<Map<String, Object>> ret= PAGE.stream(con, xml,path,params);
        return ret;
   }
           
   public default List<Map<String, Object>> list(String path, Map<String, Object> params){
       Connection con=getConnection();
        Document   xml=getDocument();
        List<Map<String, Object>> ret= PAGE.list(con, xml,path,params);
        return ret;
   }
}
