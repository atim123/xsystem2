/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http.actions;

import java.util.Map;
import org.w3c.dom.Element;
import org.xsystem.sql2.http.Executer;
import org.xsystem.utils.XMLUtil;

/**
 * EchoExcecuter
 * @author atimofeev
 */
public class DMLExecuter implements Executer {
    protected String path=null;
    
    public void parse(Element element){
       XMLUtil.elements(element).forEach(chldelement -> {
           String attrName = chldelement.getLocalName();
           switch (attrName) {
               case "path": {
                   path = XMLUtil.getContentText(chldelement);
                   break;
               }
           }
        }); 
    }
    
    @Override
    public Object execute(Map params) throws RuntimeException {
        Object ret=Executer.dml(path, params);
        return ret;
    }
}
