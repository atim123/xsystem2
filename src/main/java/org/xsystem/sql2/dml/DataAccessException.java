/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.dml;

import java.sql.SQLException;

/**
 *
 * @author atimofeev
 */
public class DataAccessException extends RuntimeException{
    public int errorCode;
    public String state;

    public DataAccessException() {
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
        getCodeAndState(cause);
    }

    private void getCodeAndState(Throwable cause) {
        if (cause instanceof SQLException) {
            errorCode = ((SQLException) cause).getErrorCode();
            state = ((SQLException) cause).getSQLState();
        } else {
            errorCode = -1;
            state = cause.getMessage();
        }
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getState() {
        return state;
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }

    public DataAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return new StringBuilder(getClass().getName()).append('[').append(errorCode).append(',').append(state).append("]: ").append(getLocalizedMessage()).toString();
    }
}
