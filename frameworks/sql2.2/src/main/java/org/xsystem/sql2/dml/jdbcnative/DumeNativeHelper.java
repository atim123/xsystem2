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
public class DumeNativeHelper extends AbstactNativeHelper{

    public DumeNativeHelper(DmlCommand dmlCommand) {
        super(dmlCommand);
    }

    @Override
    public Struct createStructure(Connection con, String baseTypeName, Map elements) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map createMap(Connection con, Struct struct, String baseTypeName) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List createList(Connection con, Array array, String baseTypeName) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Array createNamedArray(Connection con, String baseTypeName, List elements) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public  Object getOTHER(Connection con,Object retType){
        return retType;
    }
    
    public  void    setJSONPARAM(PreparedStatement ps,int pram,Object value) {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates. 
    }
}
