/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http.impl;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.xsystem.sql2.http.ConnectionManager;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author atimofeev
 */
public class JNDIConnectionManager implements ConnectionManager{
   @Override
    public Connection create(String dsName) {
        try {
            InitialContext cxt = new InitialContext();

            DataSource ds = (DataSource) cxt.lookup(dsName);
            Connection ret = ds.getConnection();
            return ret;
        } catch (NamingException | SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

  /*  @Override
    public void close(Connection conection) {
        Auxilary.close(conection);
    }
  */ 
}
