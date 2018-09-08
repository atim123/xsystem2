/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.xsystem.sql2.http.ActionEventListener;
import org.xsystem.sql2.http.Executer;

/**
 *
 * @author atimofeev
 */
public class ActionExecuter {
    String location;
    String connectionName;
    Pattern pattern;
    Executer action;
    Map<String, String> contextParams = new HashMap();
    boolean form = false;
    boolean multipart=false;
    
    ActionEventListener  beforeEvent=null;
    ActionEventListener  afterEvent =null;     
            
    
    FileFormat fileFormat;

    public void setBeforeEvent(ActionEventListener  beforeEvent){
        this.beforeEvent=beforeEvent;
    }
    
    public ActionEventListener getBeforeEvent(){
        return this.beforeEvent;
    }
    
    public void setAfterEvent(ActionEventListener  afterEvent){
        this.afterEvent=afterEvent;
    }
    
    public ActionEventListener getAfterEvent(){
        return this.afterEvent;
    }
    
    
    public FileFormat getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
    }

    public boolean isForm() {
        return form;
    }

    public void setForm(boolean form) {
        this.form = form;
    }

    public boolean isMultipart() {
        return multipart;
    }

    public void setMultipart(boolean multipart) {
        this.multipart = multipart;
    }
    
    
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Executer getAction() {
        return action;
    }

    public void setAction(Executer action) {
        this.action = action;
    }

    public Map<String, String> getContextParams() {
        return contextParams;
    }
}
