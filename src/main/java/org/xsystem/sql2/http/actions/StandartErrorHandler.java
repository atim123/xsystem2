/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http.actions;

import java.util.Map;
import org.xsystem.sql2.http.ErrorHandler;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author atimofeev
 */
public class StandartErrorHandler implements ErrorHandler{

    @Override
    public Object handler(Throwable e) {
        e.printStackTrace();
        Map errRet = Auxilary.makeJsonThrowable(e);
        return errRet;
    }
    
}
