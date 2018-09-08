/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.parser;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrey Timofeev
 */
public class Token {
   public static enum TokenType {
        EOF,
        IDENTIFIER,
        STRING,
        INTEGER,
        NUMBER,
        FREEVARIIABLE,
        RESERVED_WORD,
        DELIMETER
    };
    
    private TokenType tokenType;
    private final String content;
    
    public Token(TokenType tokenType, String content) {
        this.tokenType = tokenType;
        this.content = content;
    }
    
    public boolean isTokenType(TokenType test) {
        if (test == TokenType.NUMBER && (tokenType == TokenType.INTEGER || tokenType == TokenType.NUMBER)) {
            return true;
        }
        boolean ret = test == tokenType;
        return ret;
    }
    
    public String getTokenTypeName() {
        if (null != tokenType) switch (tokenType) {
           case EOF:
               return "конец потока обработки";
           case IDENTIFIER:
               return "идентифмкатор";
           case STRING:
               return "строка";
           case INTEGER:
               return "целое число";
           case NUMBER:
               return "число";
           case FREEVARIIABLE:
               return "свободная переменая";
           case RESERVED_WORD:
               return "служебное слово";
           case DELIMETER:
               return "разделитель";
           default:
               break;
       }
        return "????";
    }
    
    public boolean isDelimetr(String del) {
        if (tokenType == TokenType.DELIMETER) {
            if (content.equals(del)) {
                return true;
            }
        }
        return false;
    }
    
    public TokenType getTokenType() {
        return tokenType;
    }

    public String getContent() {
        return content;
    }
    
    public Number getNumber() {
       try {
           double d = NumberFormat.getNumberInstance(Locale.US).parse(content).doubleValue();
           return d;
       } catch (ParseException ex) {
          throw new Error(ex);
       }
    }
    
    @Override
    public String toString() {
        if (content != null) {
            return String.format(" %s [%s] ", getTokenTypeName(), content);
        } else {
            return String.format(" %s", getTokenTypeName());
        }
    } 
}
