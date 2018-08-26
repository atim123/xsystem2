/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.page;

/**
 *
 * @author atimofeev
 */
public class ContextDefinition {
    String name;
    String type;
    String objecttype;
    String ifExpr;
    String value;
    boolean in;
    boolean out;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
    public String getObjectType() {
        return objecttype;
    }

    public void setObjectType(String type) {
        this.objecttype = type;
    }
    
    public String getIfExpr() {
        return ifExpr;
    }

    public void setIfExpr(String ifExpr) {
        this.ifExpr = ifExpr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
