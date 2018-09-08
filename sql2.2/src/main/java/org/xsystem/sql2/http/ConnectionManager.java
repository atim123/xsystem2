/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http;

import java.sql.Connection;
/**
 *
 * @author atimofeev
 */
public interface ConnectionManager {
    public Connection create(String name);
 //   public void close(Connection conection);
}
