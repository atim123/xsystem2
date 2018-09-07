/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author atimofeev
 */
public class RestApiTemplate extends HttpServlet {

    public final static GsonBuilder gsonBuilder = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .registerTypeHierarchyAdapter(byte[].class,new BytesSerializer());
            
    protected static void prepareJsonResponse(HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
    }
    
    protected static void prepareXmlResponse(HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","application/xml; charset=utf-8");
     }
    
    protected static void write(byte b[], ServletOutputStream out) throws IOException {
        out.write(b);
    }

    protected static void write(String s, ServletOutputStream out) throws IOException {
        out.write(s.getBytes("UTF-8"));
    }

    protected static void writeJson(Object o, ServletOutputStream out) throws IOException {
        Gson gson = gsonBuilder.create();
        String s = gson.toJson(o);
        write(s, out);
    }

    protected  static void makeJsonSuccessMap(Map<String, Object> responce,ServletOutputStream out) throws IOException{
        Map ret= Auxilary.makeJsonSuccessMap(responce);
        writeJson(ret,out);
    }
    
    protected static void error(Throwable e, ServletOutputStream out) throws IOException {

        e.printStackTrace();
        Map errRet = Auxilary.makeJsonThrowable(e);
           
        writeJson(errRet, out);
    }

    protected static String getContentAsString(ServletInputStream input) throws IOException {
        byte[] b = IOUtils.toByteArray(input);
        String ret = new String(b, "UTF-8");
        return ret;
    }

    protected static  List<String> getGroups(Matcher matcher) {
        List ret = new ArrayList();
        int count = matcher.groupCount();
        for (int i = 0; i < count; i++) {
            String value = matcher.group(i + 1);
            ret.add(value);
        }
        return ret;
    }
    
    protected Map getJsonEmptyIsMap(ServletInputStream input) throws IOException{
        String json = getContentAsString(input);
        if (Auxilary.isEmptyOrNull(json)) {
            return new HashMap();
        }
        Gson gson = gsonBuilder.create();
        Object jsonContext = gson.fromJson(json, Object.class);
        return (Map)jsonContext;
    }
    
    protected Object getJson(ServletInputStream input) throws IOException {
        String json = getContentAsString(input);
        Gson gson = gsonBuilder.create();
        Object jsonContext = gson.fromJson(json, Object.class);
        return jsonContext;
    }
    
    protected String getRequestParameter(HttpServletRequest request,String paramName,String def){
        String ret = request.getParameter(paramName);
        if (Auxilary.isEmptyOrNull(ret)){
            return def;
        }
        return ret;
    }
    
    
    protected boolean chekMatcher(Matcher matcher, Pattern key) {
        boolean ret = matcher.reset().usePattern(key).find();
        return ret;
    }
}
