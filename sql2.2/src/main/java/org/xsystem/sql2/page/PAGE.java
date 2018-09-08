/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.page;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xsystem.sql2.dml.DmlCancel;
import org.xsystem.sql2.dml.ParamsHelper;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author atimofeev
 */
public class PAGE implements ParamsHelper{
    
    public static Map<String, Object> params(Object... args) {
        return Auxilary.newMap(args);
    }
    
    public static class Dog extends DmlCancel{}
  /*  public static List list(Object... args) {
       List res = new ArrayList();
        if (args != null) {
            res.addAll(Arrays.asList(args));
        }
        return res;
    }
  */
    
    public static void prepare(SqlAction action, Document xml, String path) {
        Element elem = getNode(path, xml);
        if (elem==null){
            throw new Error("Can't find sql action path=["+path+"]");
        }
        buildContext(action, elem);
        Element sqlElement = (Element) XMLUtil.getNoge("./sql", elem);
        if (sqlElement==null){
           throw new RuntimeException("in ["+path+"] expected <sql> teg" ); 
        }
        String elsql = XMLUtil.getContentText(sqlElement);
        String strnamed = elem.getAttribute("namedparameters");
        if (!Auxilary.isEmptyOrNull(strnamed)) {
            boolean v = strnamed.equalsIgnoreCase("true");
            action.setNamedparameters(v);
        }
        boolean singleRow=true;
        String singleRowStr=elem.getAttribute("singlerow");
        if (!Auxilary.isEmptyOrNull(singleRowStr)){
            singleRow = strnamed.equalsIgnoreCase("true");
        }
        action.setSingleRow(singleRow);
        elsql = elsql.trim();
        action.setSql(elsql);
        Element skipElement=(Element) XMLUtil.getNoge("./skip", elem);
        if (skipElement!=null){
           String elskip = XMLUtil.getContentText(skipElement); 
           action.setSkip(elskip);
        } 
        Element totalElement=(Element) XMLUtil.getNoge("./total", elem);
        if (totalElement!=null){
            String eltotal = XMLUtil.getContentText(totalElement); 
            action.setTotal(eltotal);
        }
    }
    
    public static void batch(Connection connection, Document xml, String path, List<Map<String, Object>> params){
        batch(connection,xml,path, params,null);
    }
    
    public static void batch(Connection connection, Document xml, String path, List<Map<String, Object>> params,Dog dog){
        SqlBatchAction action = new SqlBatchAction();
        prepare(action, xml, path);
        action.batch(connection, params,dog);
    }
    
    public static String SQLTEXT(Connection connection, Document xml, String path, Map<String, Object> params){
        SqlTextAction action=new SqlTextAction();
        prepare(action, xml, path);
        String ret=(String)action.exec(connection, params,null);
        return ret;
    }
    
    public static Map<String, Object> rsMeta(Connection connection, Document xml, String path, Map<String, Object> params){
        SqlRsMetaAction action=new SqlRsMetaAction();
        prepare(action, xml, path);
        Map<String, Object> ret=(Map)action.exec(connection, params,null);
        return ret;
    }
    
    public static Object dml(Connection connection, Document xml, String path, Map<String, Object> params){
        return dml(connection,xml,path, params,null);
    }
    
    public static Object dml(Connection connection, Document xml, String path, Map<String, Object> params,Dog dog) {
        SqlDmlAction action = new SqlDmlAction();
        prepare(action, xml, path);
        return action.exec(connection, params,dog);
    }
    
    public static Map<String, Object> row(Connection connection, Document xml, String path, Map<String, Object> params) {
        return row(connection,xml,path,params,null);
    }
    
    public static Map<String, Object> row(Connection connection, Document xml, String path, Map<String, Object> params,Dog dog) {
        SqlRowAction action = new SqlRowAction();
        prepare(action, xml, path);

        return (Map) action.exec(connection, params,dog);
    }
    
     public static Object rowValue(Connection connection, Document xml, String path, Map<String, Object> params, String rowName) {
       return rowValue(connection,  xml, path, params,rowName,null); 
     }
     
    public static Object rowValue(Connection connection, Document xml, String path, Map<String, Object> params, String rowName,Dog dog) {
        Map<String, Object> retrow = row(connection, xml, path, params,dog);
        if (retrow != null) {
            return retrow.get(rowName);
        }
        return null;
    }
    
    public static Stream<Map<String, Object>> stream(Connection connection, Document xml, String path,Long skip, Integer total, Map<String, Object> params) {
       return stream(connection,xml,path,skip,total,params,null);
    }
    
     public static Stream<Map<String, Object>> stream(Connection connection, Document xml, String path,Long skip, Integer total, Map<String, Object> params,Dog dog) {
        SqlStreamAction action = new SqlStreamAction();
        prepare(action, xml, path);
        return  action.execQuery(connection,skip,total, params,dog);
    }

   public static Stream<Map<String, Object>> stream(Connection connection, Document xml, String path, Map<String, Object> params) {
       return stream(connection,xml,path, params,null);
   } 
     
    public static Stream<Map<String, Object>> stream(Connection connection, Document xml, String path, Map<String, Object> params,Dog dog) {
        SqlStreamAction action = new SqlStreamAction();
        prepare(action, xml, path);
        return  action.execQuery(connection,null,null, params,dog);
    } 
    
    public static List<Map<String, Object>> list(Connection connection, Document xml, String path,Long skip, Integer total, Map<String, Object> params) {
        return list(connection,xml,path,skip,total, params,null); 
    }
    
    public static List<Map<String, Object>> list(Connection connection, Document xml, String path,Long skip, Integer total, Map<String, Object> params,Dog dog) {
        return stream(connection, xml, path,skip,total, params,dog)
                .collect(Collectors.toList());
    }

    public static List<Map<String, Object>> list(Connection connection, Document xml, String path, Map<String, Object> params) {
        return  list(connection,xml,path,params,null) ;
    }
    
    public static List<Map<String, Object>> list(Connection connection, Document xml, String path, Map<String, Object> params,Dog dog) {
        return stream(connection, xml, path,null,null, params,dog)
                .collect(Collectors.toList());
    }
    
    //------------Private--------------------------------------------------------
    static void buildContext(SqlAction action, Element elem) {//throws XPathExpressionException {
        buildGlobalContext(action, elem);
        buildLocalContext(action, elem);
        buildParams(action, elem);
    }

    static void buildGlobalContext(SqlAction action, Element elem) { //throws XPathExpressionException {
        Element root = elem.getOwnerDocument().getDocumentElement();

        Element definitionsElem = (Element) XMLUtil.getNoge("./definitions", root);
        if (definitionsElem != null) {
            buildLocalContext(action,definitionsElem);
        }
    }

    static void buildLocalContext(SqlAction action, Element elem) {

        NodeList rowList = XMLUtil.getNogeList("./define", elem);
        for (int i = 0; i < rowList.getLength(); i++) {
            ContextDefinition contextDefinition = new ContextDefinition();

            Element item = (Element) rowList.item(i);
            String name = item.getAttribute("name");
            String ifEl = item.getAttribute("if");
            String value = XMLUtil.getContentText(item).trim();

            contextDefinition.setName(name);

            if (!Auxilary.isEmptyOrNull(ifEl)) {
                contextDefinition.setIfExpr(ifEl);
            }

            

            if (!Auxilary.isEmptyOrNull(value)) {
                contextDefinition.setValue(value);
            }
            action.getDefinitions().add(contextDefinition);
        }
    }

    static void buildParams(SqlAction action, Element elem) {

        NodeList rowList = XMLUtil.getNogeList("./parameter", elem);

        // Object[] sqlPrm = new Object[rowList.getLength()];
        for (int i = 0; i < rowList.getLength(); i++) {
            Element item = (Element) rowList.item(i);
            String name = item.getAttribute("name");
            String inParam=item.getAttribute("in");
            String outParam=item.getAttribute("out");
            String type = item.getAttribute("type");
            
            String objecttype=item.getAttribute("objecttype");
            String arraytype =item.getAttribute("arraytype");
            
            String ifEl = item.getAttribute("if");
            String value = item.getAttribute("value");

            ContextDefinition contextDefinition = new ContextDefinition();

            contextDefinition.setName(name);

            if (!Auxilary.isEmptyOrNull(arraytype)&&Auxilary.isEmptyOrNull(type)) {
                type="ARRAY";
                contextDefinition.setObjectType(arraytype);
            }
         
            if (!Auxilary.isEmptyOrNull(objecttype)){
                contextDefinition.setObjectType(objecttype);
                if (Auxilary.isEmptyOrNull(type)){
                    type="STRUCT";
                }
            }
            
            if (!Auxilary.isEmptyOrNull(ifEl)) {
                contextDefinition.setIfExpr(ifEl);
            }

            if (!Auxilary.isEmptyOrNull(type)) {
                contextDefinition.setType(type);
            }

            if (!Auxilary.isEmptyOrNull(value)) {
                contextDefinition.setValue(value);
            }
            boolean in;
            boolean out;
                  
            if (Auxilary.isEmptyOrNull(inParam)&&Auxilary.isEmptyOrNull(outParam)){
                in=true;
                out=false;
            } else if (Auxilary.isEmptyOrNull(inParam)&&!Auxilary.isEmptyOrNull(outParam)){
                out=Boolean.parseBoolean(outParam);
                in=true;
                if (out){
                  in=false;  
                }
            } else if (!Auxilary.isEmptyOrNull(inParam)&&Auxilary.isEmptyOrNull(outParam)){
                in=Boolean.parseBoolean(inParam);
                out=true;
                if (in){
                  out=false;  
                }
            } else {
                in=Boolean.parseBoolean(inParam);
                out=Boolean.parseBoolean(outParam);
            }
            
            contextDefinition.setIn(in);
            contextDefinition.setOut(out);
         
            action.getParams().add(contextDefinition);
        }
    }

    
    
    
    static Element getNode(String path, Document xml) {
        Element root = xml.getDocumentElement();
        Element elem = (Element) XMLUtil.getNoge(path, root);
        return elem;
    }
}
