/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http;

import java.util.Base64;

/**
 *
 * @author atimofeev
 */
public class Base64Decode {
   public static byte[] decode(String src) {
       byte[] ret=Base64.getDecoder().decode(src);
       return ret;
    } 
}
