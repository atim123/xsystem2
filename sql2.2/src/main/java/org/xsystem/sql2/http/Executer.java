/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xsystem.sql2.page.PAGE;
/**
 *
 * @author atimofeev
 */
public interface Executer {
   public Object execute(Map params) throws RuntimeException;
   
     
   public void parse(Element element);
   
   
   
   public static Object dml(String path, Map<String, Object> params) {
      Enviroment env= Enviroment.getEnviroment();
      Connection connection=env.getConnection();
      Document document=env.getDocument();
      Object ret=PAGE.dml(connection, document, path, params);
      return ret;
   }
   
   public static Map<String, Object> row(String path, Map<String, Object> params) {
      Enviroment env= Enviroment.getEnviroment();
      Connection connection=env.getConnection();
      Document document=env.getDocument();
      Map<String, Object> ret=PAGE.row(connection, document, path, params);
      return ret;
   }
   
   public static Object rowValue(String path, Map<String, Object> params,String rowName) {
      Enviroment env= Enviroment.getEnviroment();
      Connection connection=env.getConnection();
      Document document=env.getDocument();
      Object ret=PAGE.rowValue(connection, document, path, params,rowName);
      return ret;
   }
   
   public static Stream<Map<String, Object>> stream(String path, Map<String, Object> params) {
      Enviroment env= Enviroment.getEnviroment();
      Connection connection=env.getConnection();
      Document document=env.getDocument();
      Stream<Map<String, Object>> ret=PAGE.stream(connection, document, path, params);
      return ret;
   }
   
   public static List<Map<String, Object>> list(String path,Long skip, Integer total, Map<String, Object> params) {
      Enviroment env= Enviroment.getEnviroment();
      Connection connection=env.getConnection();
      Document document=env.getDocument();
      List<Map<String, Object>> ret=PAGE.list(connection, document,path,skip,total, params);
      return ret;
   }
   
   public static List<Map<String, Object>> list(String path, Map<String, Object> params) {
      Enviroment env= Enviroment.getEnviroment();
      Connection connection=env.getConnection();
      Document document=env.getDocument();
      List<Map<String, Object>> ret=PAGE.list(connection, document, path, params);
      return ret;
   }
}
