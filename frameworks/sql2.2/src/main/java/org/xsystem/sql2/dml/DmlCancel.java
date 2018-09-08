/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.dml;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Andrey Timofeev
 */
public class DmlCancel {
    Statement stmt=null;
    
    public synchronized  void cancel(){
        if (stmt!=null){
            try {
                stmt.cancel();
            } catch (SQLException ex) {
            }
        }
    }
    
    synchronized void setStatement(Statement stmt){
        this.stmt=stmt;
    }
}
