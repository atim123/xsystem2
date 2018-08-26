/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http.impl;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xsystem.sql2.http.AfterCreateConnectionEventListener;
import org.xsystem.sql2.http.BeforeCloseConnectionEventListener;
import org.xsystem.sql2.http.ConnectionManager;
import org.xsystem.sql2.http.ErrorHandler;
import org.xsystem.sql2.http.actions.StandartErrorHandler;

/**
 *
 * @author atimofeev
 */
public class Config {
    Map<String, String> connections = new HashMap();
   Map<String, String> fileStorages =new HashMap();
    Map<String, String> objectNames =new HashMap();
   List<ActionExecuter> actions = new ArrayList();
   ConnectionManager connectionManager;
   Map<String, AfterCreateConnectionEventListener> afterCreateConnectionEventListeners = new HashMap();
   Map<String, BeforeCloseConnectionEventListener> beforeCloseConnectionEventListeners = new HashMap();
   ErrorHandler errorHandler=null;
       
   
   public Config(){
       
   }

   public Map<String, String> getFileStorages() {
        return fileStorages;
    }
   
   public void setFileStorages(Map<String, String> fileStorages) {
        this.fileStorages = fileStorages;
    }
   
    public Map<String, String> getConnections() {
        return connections;
    }

    public void setConnections(Map<String, String> connections) {
        this.connections = connections;
    }

    public Map<String, String> getObjectNames(){
        return this.objectNames;
    }
    
    public void setObjectNames(Map<String, String> objectNames){
        this.objectNames=objectNames;
    }
    
    public Map<String, AfterCreateConnectionEventListener> getAfterCreateConnectionEventListeners() {
        return afterCreateConnectionEventListeners;
    }

    public void setAfterCreateConnectionEventListeners(Map<String, AfterCreateConnectionEventListener> afterCreateConnectionEventListeners) {
        this.afterCreateConnectionEventListeners = afterCreateConnectionEventListeners;
    }

    public Map<String, BeforeCloseConnectionEventListener> getBeforeCloseConnectionEventListeners() {
        return beforeCloseConnectionEventListeners;
    }

    public void setBeforeCloseConnectionEventListeners(Map<String, BeforeCloseConnectionEventListener> beforeCloseConnectionEventListeners) {
        this.beforeCloseConnectionEventListeners = beforeCloseConnectionEventListeners;
    }


    
    
    public List<ActionExecuter> getActions() {
        return actions;
    }

    public void setActions(List<ActionExecuter> actions) {
        this.actions = actions;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    
    public Connection getConnection(String connectionName){
        String dsName = connections.get(connectionName);
        Connection connection = connectionManager.create(dsName);
        return connection;
    }
    
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler=errorHandler;
    }
    
    public ErrorHandler getErrorHandler(){
        return errorHandler;
    } 
    
}
