/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.dml.jdbcnative;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.List;
import java.util.Map;
import org.xsystem.sql2.dml.DmlCommand;

/**
 *
 * @author atimofeev
 */
public abstract class AbstactNativeHelper {
    protected DmlCommand dmlCommand;
    
    public AbstactNativeHelper(DmlCommand dmlCommand){
       this.dmlCommand= dmlCommand;
    }
    
    public abstract Struct createStructure(Connection con, String baseTypeName, Map elements) throws SQLException;
    public abstract Map createMap(Connection con,Struct struct,String baseTypeName) throws SQLException;
    
    public abstract List createList(Connection con, Array array,String baseTypeName) throws SQLException;
    public abstract Array createNamedArray(Connection con, String baseTypeName, List elements) throws SQLException;

    public abstract Object getOTHER(Connection con,Object retType);
    public abstract void    setJSONPARAM(PreparedStatement ps,int pram,Object value) throws SQLException;
}
