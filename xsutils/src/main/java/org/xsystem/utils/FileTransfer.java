/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author atimofeev
 */
public class FileTransfer implements Serializable {

    byte[] data = null;
    String fileName = null;
    String contentType = null;
    String fileType = null;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setDataAsString(String data) {
        if (data == null) {
            this.data = null;
            return; 
        }
        try {
            this.data = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex);
        }
    }

    public String getDataAsString() {
        if (this.data == null) {
            return null;
        }
        try {
            String ret = new String(this.data, "UTF-8");
            return ret;
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getSize() {
        Integer ret = null;
        if (data != null) {
            ret = data.length;
        }
        return ret;
    }
}
