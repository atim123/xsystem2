/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http.actions;

import java.util.Map;
import org.w3c.dom.Element;
import org.xsystem.sql2.http.Executer;

/**
 *
 * @author atimofeev
 */
public class EchoExcecuter implements Executer{

    
    
    @Override
    public Object execute(Map params) throws RuntimeException {
        return params;
    }

    @Override
    public void parse(Element element) {
    }
    
}
