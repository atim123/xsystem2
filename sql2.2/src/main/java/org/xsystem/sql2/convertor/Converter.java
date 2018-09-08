/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.convertor;

import java.io.Serializable;

/**
 *
 * @author atimofeev
 */
public interface Converter extends Serializable{
   
    public <T> T convert(Object value, Class<T> type); 
}
