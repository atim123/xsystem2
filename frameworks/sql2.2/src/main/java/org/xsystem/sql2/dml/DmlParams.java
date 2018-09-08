/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.dml;

/**
 *
 * @author atimofeev
 */
public class DmlParams implements ParamsHelper{
    String name;
    int   jdbcType;
    String objectType;
    boolean in;
    boolean out;
  /*  
    public DmlParams(String name,int    jdbcType, boolean in, boolean out){
       this.name=name;
       this.jdbcType=jdbcType;
       this.in=in;
       this.out=out;
       objectType=null;
    }
    
    public DmlParams(String name,int  jdbcType,String objectType, boolean in, boolean out){
       this.name=name;
       this.jdbcType=jdbcType;
       this.in=in;
       this.out=out;
       this.objectType=objectType;
    }
  */  
    public DmlParams(){
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
    
    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }
}
