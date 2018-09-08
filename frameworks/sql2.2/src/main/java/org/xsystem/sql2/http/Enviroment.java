/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http;

import java.io.File;
import java.sql.Connection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.w3c.dom.Document;

/**
 *
 * @author atimofeev
 */
public interface Enviroment {
    public static String FILEPRFX="file://";
    
    
    public static Enviroment getEnviroment(){
       return PageServlet2.getEnviroment();
    }
    
    public static boolean isFile(String path) {
        if (path != null) {
            return path.startsWith(FILEPRFX);
        }
        return false;
    }
    
    public HttpServletRequest getHttpRequest();
    
    public Connection getConnection();
    
    public Document  getDocument();
    
    public File getFile(String fname);
    
    public Map<String, String> getObjectNames();
}
