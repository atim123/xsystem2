/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.parser;

/**
 *
 * @author Andrey Timofeev
 */
public class ParserException extends RuntimeException{
    private int line = 0;
    private int pos = 0;
    private String lastString="";
    private Token token = null;
    
    public ParserException(int line, int pos, String message) {
        super(message);
        this.line = line;
        this.pos = pos;
    }
    
    public ParserException(int line, int pos, String message, Token token) {
        super(message);
        this.line = line;
        this.pos = pos;
        this.token = token;
    }

    public ParserException(Scaner scaner, String message) {
        super(message);
        if (scaner == null) {
            throw new IllegalArgumentException("Scanner can not be null!");
        }
        this.line = scaner.getLine();
        this.pos = scaner.getPos();
        this.lastString=scaner.getLastString();
    }
    
    public ParserException(Scaner scaner, String message, Token token) {
        super(message);
        if (scaner == null) {
            throw new IllegalArgumentException("Scanner can not be null!");
        }
        this.line = scaner.getLine();
        this.pos = scaner.getPos();
        this.lastString=scaner.getLastString();
        this.token = token;
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }

    public Token getToken() {
        return token;
    }
    
    public String getLastString() {
        return lastString;
    }
    /**
     * @return отформатированную информацию об ошибке R:<строка>,C:<столбец>,T:<текущий токен>,ERROR:<информация об ошибке>
     */
    @Override
    public String getMessage() {
        if (lastString.equals(""))
          return String.format("строка:%d,позиция:%d,встретился %s, %s", line, pos, token, super.getMessage());
        else
          return String.format("строка:%d,позиция:%d [...%s...],встретился %s, %s", line, pos, lastString, token, super.getMessage());  
    }
}
