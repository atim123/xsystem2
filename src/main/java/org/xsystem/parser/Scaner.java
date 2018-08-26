/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Stack;
import org.apache.commons.lang3.StringUtils;
/**
 *
 * @author Andrey Timofeev
 */
public class Scaner {
    private PushbackReader input;
    private Stack<Token> stack = new Stack<Token>();
    private int pos = 0;
    private int line = 1;
    private String delimetrs = StringUtils.EMPTY;
    private String lineBreak = System.getProperty("line.separator");
    private StringBuffer lastString = new StringBuffer();
    
    final void init(InputStream in, String encoding){
        try {
            Reader r = new InputStreamReader(in, encoding);
            input = new PushbackReader(r);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public Scaner(InputStream in, String encoding) {
        init(in,encoding);
    
    }

    public Scaner(InputStream in)  {
        init(in, "UTF-8");
    }
    /*
    public Scaner(File f)  {
        InputStream in=null;
        try {
            in = new FileInputStream(f);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        
        try {
           init(in, "UTF-8"); 
        } finally {
            Auxilary.close(in);
        }
    }
    */
    
    public Scaner(String code)  {
       try{
        init(new ByteArrayInputStream(code.getBytes("UTF-8")),"UTF-8");
       } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void setDelimetrs(String delimetrs) {
        this.delimetrs = delimetrs;
    }
    
    public int getPos() {
        return pos;
    }

    public int getLine() {
        return line;
    }
    
    public String getLastString(){ 
      return lastString.toString();
    }
    
    private int getChar() throws IOException {
        int ret = input.read();
        lastString.append((char)ret);
        pos++;
        return ret;
    }

    private void ungetChar(int chr) throws IOException {
        input.unread(chr);
        lastString=lastString.deleteCharAt(lastString.length()-1);
        pos--;
    }
    
    private void skipWhite() throws IOException {
        int chr;
        while (true) {
            chr = getChar();
            if (lineBreak.indexOf(chr)>-1) {
                lastString=new StringBuffer();
                pos = 1;
                line++;
                continue;
            } else if (Character.isWhitespace(chr)) {
                continue;
            } else {
                break;
            }
        }
        if (chr != -1) {
            ungetChar(chr);
        }
    }
    
    private void skipComment() throws IOException {
        int chr;
        while (true) {
            chr = getChar();
            if (lineBreak.indexOf(chr)>-1) {
                lastString=new StringBuffer();
                pos = 1;
                line++;
                continue;
            }
            if (chr == '*') {
                int nextChar = getChar();
                if (nextChar == -1) {
                    throw new IOException("Неожиданный конец файла");
                }
                if (nextChar == '/') {
                    break;
                }
            }
        }
    }
    
    private Token readIdentificator() throws IOException {
        StringBuffer buffer = new StringBuffer();

        int chr = getChar();
        //String buffer =""+(char) chr;
        buffer.append((char) chr);

        while (true) {
            chr = getChar();
            if (!Character.isUnicodeIdentifierPart(chr)) {
                break;
            }
            // buffer=buffer+(char)chr;
            buffer.append((char) chr);
        }
        if (chr != -1) {
            ungetChar(chr);
        }
        String ret = buffer.toString();
        Token token = new Token(Token.TokenType.IDENTIFIER, ret);
        return token;
    }
    
    private Token readNumber() throws IOException {
        boolean isPoint = false;
        int chr;
        StringBuffer buffer = new StringBuffer();
        while (true) {
            chr = getChar();
            if (Character.isDigit(chr)) {
                buffer.append((char) chr);
            } else if (chr == '.') {
                if (isPoint) {
                    break;
                }
                isPoint = true;
                buffer.append((char) chr);
            } else {
                break;
            }
        }
        if (chr != -1) {
            ungetChar(chr);
        }
        String ret = buffer.toString();
        Token token = new Token((isPoint) ? Token.TokenType.NUMBER : Token.TokenType.INTEGER, ret);
        return token;
    }
    
    private Token readString(char Q) throws IOException {
        int chr;
        StringBuffer buffer = new StringBuffer();
        while (true) {
            chr = getChar();
            if (chr == Q) {
                break;
            }
            if (chr == -1) {
                throw new IOException("Неожиданный конец файла");
            }
            buffer.append((char) chr);
            if (lineBreak.indexOf(chr)>-1) {
                pos = 1;
                line++;
            }
        }
        String ret = buffer.toString();
        Token token = new Token(Token.TokenType.STRING, ret);
        return token;
    }
    
    String getDelimetr(int chr) {
        int idx = delimetrs.indexOf(chr);
        if (idx == -1) {
            return null;
        }
        char c = delimetrs.charAt(idx);
        String ret = "" + c;
        return ret;
    }
    
    public Token nextToken() throws  ParserException {
       try{
        if (!stack.empty()) {
            Token token = stack.pop();
            return token;
        }
        skipWhite();
        int chr = getChar();
        if (chr == -1) {
            return new Token(Token.TokenType.EOF, "end of file");
        }
        if (chr == '/') {
            int nextchar = getChar();
            if (nextchar == '*') {
                skipComment();
                return nextToken();
            } else {
                ungetChar(nextchar);
            }
        }
        if (chr=='?'){
           Token token = new Token(Token.TokenType.FREEVARIIABLE,"?");
           return token;
        }
        if (Character.isJavaIdentifierStart(chr)&&(chr!='$')) {
            ungetChar(chr);
            Token token = readIdentificator();
            return token;
        }

        if (Character.isDigit(chr)) {
            ungetChar(chr);
            Token token = readNumber();
            return token;
        }

        if (chr == '\'' || chr == '"') {
            return readString((char) chr);
        }
        

        String delim = getDelimetr(chr);
        if (delim != null) {
            return new Token(Token.TokenType.DELIMETER, delim);
        }
        String schr = "" + (char) chr;
        throw new ParserException(this, "Недопустимый символ " + schr, null);
       }catch(IOException ex){
           throw new ParserException(this, "Системная ошибка "+ex.toString());
       }  
    }

    
    
    public void putBack(Token token) {
        stack.push(token);
    }
}
