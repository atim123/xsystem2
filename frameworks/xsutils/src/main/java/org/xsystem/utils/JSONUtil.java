/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.xsystem.http.BytesSerializer;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Andrey Timofeev
 */
public class JSONUtil {

    public final static GsonBuilder gsonBuilder = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .registerTypeHierarchyAdapter(byte[].class, new BytesSerializer());

    public static Object getJson(String json) {
        Gson gson = gsonBuilder.create();
        Object jsonContext = gson.fromJson(json, Object.class);
        return jsonContext;
    }

    
    public static Map getJsonEmptyIsMap(String json){
        if (Auxilary.isEmptyOrNull(json)) {
            return new HashMap();
        }
        Gson gson = gsonBuilder.create();
        Object jsonContext = gson.fromJson(json, Object.class);
        return (Map)jsonContext;
    }
    
    public static  Object getJson(InputStream input) {
        try {
            String json = IOUtils.toString(input, "UTF-8");
            Gson gson = gsonBuilder.create();
            Object jsonContext = gson.fromJson(json, Object.class);
            return jsonContext;
        } catch (JsonSyntaxException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    
    
    public static  Object getJsonFromResource(String resurce){
       String str= Auxilary.getResourceAsStringE(resurce);
       Object ret=getJson(str);
       return ret;
    }
    
            
            
    public static  Object getJson(File f) {
        InputStream is=null;
        try {
            is = new FileInputStream(f);
            Object ret=getJson(is);
            return ret;
        } catch (FileNotFoundException ex) {
            throw new Error(ex);
        } finally {
            Auxilary.close(is);
        }
    }
    
    public static String toJsom(Object obj){
        Gson gson = gsonBuilder.create();
        String s = gson.toJson(obj);
        return s;
    }
    
    public static void jsomToFile(Object obj,File f){
        try {
            String json=toJsom(obj);
            FileUtils.writeStringToFile(f,json,"UTF-8");
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }
}
