/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http.impl;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.jxpath.JXPathContext;
import org.xsystem.http.RestApiTemplate;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.FileTransfer;

/**
 *
 * @author atimofeev
 */
public class HttpHelper {
    public static Map formUpload(HttpServletRequest request) throws FileUploadException, UnsupportedEncodingException {
        Map ret = new HashMap();
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        List items = upload.parseRequest(request);
        Iterator iterator = items.iterator();

        while (iterator.hasNext()) {
            FileItem item = (FileItem) iterator.next();
            String paramName = item.getFieldName();
            if (paramName.equalsIgnoreCase("skip")) {
                continue;
            }
            if (paramName.equalsIgnoreCase("total")) {
                continue;
            }
            if (item.isFormField()) {
                String svalue = item.getString("UTF-8");
                ret.put(paramName, svalue);
            } else {

                FileTransfer fileTransfer = new FileTransfer();
                String fileName = item.getName();
                String contentType = item.getContentType();
                byte[] data = item.get();
                fileTransfer.setFileName(fileName);
                String ft = Auxilary.getFileExtention(fileName);
                fileTransfer.setFileType(ft);
                fileTransfer.setContentType(contentType);
                fileTransfer.setData(data);
                ret.put(paramName, fileTransfer);
            }
        }
        return ret;
    }

    public static Map form(HttpServletRequest request) {
        Map ret = new HashMap();
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            if (key.equalsIgnoreCase("skip")) {
                continue;
            }
            if (key.equalsIgnoreCase("total")) {
                continue;
            }
            Object value = request.getParameter(key);
            ret.put(key, value);
        }
        return ret;
    }

    public static Map getParams(HttpServletRequest request) throws FileUploadException, UnsupportedEncodingException {
        if (ServletFileUpload.isMultipartContent(request)) {
            return formUpload(request);
        } else {
            return form(request);
        }

    }
// RestApiTemplate
    public static Map formJson(HttpServletRequest request) {
        Map ret = new HashMap();
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value =(String) request.getParameter(key);
            Gson gson = RestApiTemplate.gsonBuilder.create();
            Object jsonContext = gson.fromJson(value, Object.class);
            ret.put(key, jsonContext);
        }
        return ret;
    }
    
    
    public static Map formUploadJson(HttpServletRequest request) throws FileUploadException, UnsupportedEncodingException {
        Map ret = new HashMap();
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        List items = upload.parseRequest(request);
        Iterator iterator = items.iterator();

        while (iterator.hasNext()) {
            FileItem item = (FileItem) iterator.next();
            String paramName = item.getFieldName();
            
            if (item.isFormField()) {
                String svalue = item.getString("UTF-8");
                Gson gson = RestApiTemplate.gsonBuilder.create();
                Object jsonContext = gson.fromJson(svalue, Object.class);
                ret.put(paramName,jsonContext);
            } else {

                FileTransfer fileTransfer = new FileTransfer();
                String fileName = item.getName();
                String contentType = item.getContentType();
                byte[] data = item.get();
                fileTransfer.setFileName(fileName);
                String ft = Auxilary.getFileExtention(fileName);
                fileTransfer.setFileType(ft);
                fileTransfer.setContentType(contentType);
                fileTransfer.setData(data);
                ret.put(paramName, fileTransfer);
            }
        }
        return ret;
    }
    
    public static Map getMultipartJson(HttpServletRequest request) throws FileUploadException, UnsupportedEncodingException{
        if (ServletFileUpload.isMultipartContent(request)) {
            return formUploadJson(request);
        }else {
            return formJson(request);
        }
    }
    
    public static List getGroups(Matcher matcher) {
        List ret = new ArrayList();
        int count = matcher.groupCount();
        for (int i = 0; i < count; i++) {
            String value = matcher.group(i + 1);
            ret.add(value);
        }
        return ret;
    }

    public static Long getParamSkip(HttpServletRequest request) {
        Long skip = null;
        if (!ServletFileUpload.isMultipartContent(request)) {
            String s = request.getParameter("skip");
            if (!Auxilary.isEmptyOrNull(s)) {
                skip = Long.parseLong(s);
            }
        }
        return skip;
    }

    public static Integer getParamTotal(HttpServletRequest request) {
        Integer total = null;
        if (!ServletFileUpload.isMultipartContent(request)) {
            String s = request.getParameter("total");
            if (!Auxilary.isEmptyOrNull(s)) {
                total = Integer.parseInt(s);
            }
        }
        return total;
    }

    public static int getThumb(FileFormat fileFormat, Map<String, Object> reqContext) {
        if (fileFormat == null) {
            return -1;
        }
        String eval = fileFormat.getPreview();
        if (eval.isEmpty()) {
            return 200;
        }
        JXPathContext context = JXPathContext.newContext(reqContext);
        context.setLenient(true);
        Object obj = context.getValue(eval);
        if (obj == null) {
            return 200;
        }
        Integer ret =Integer.valueOf(obj.toString().trim());
                
        return ret;
    }

    public static FileTransfer getFileTransfer(FileFormat fileFormat, Object value, BiFunction<String,Object,byte[]> bi) {
        Map row = null;
        if (value instanceof List) {
            List lst = (List) value;
            if (!lst.isEmpty()) {
                Object test = lst.get(0);
                if (test instanceof Map) {
                    row = (Map) test;
                }
            }
        } else if (value instanceof Map) {
            row = (Map) value;
        }
        if (row == null) {
            return null;
        }
        String evalContenttype = fileFormat.getContenttype();
        String evalFilename = fileFormat.getFilename();
        String evalContent = fileFormat.getContent();
        String evalFormat = fileFormat.getFormat();
        JXPathContext context = JXPathContext.newContext(row);
        context.setLenient(true);
        String contenttype = (String) context.getValue(evalContenttype);
        String filename = (String) context.getValue(evalFilename);
        //byte[] content = (byte[]) context.getValue(evalContent);
        String format = (String) context.getValue(evalFormat);
        Object data =  context.getValue(evalContent);
    
        byte[] content =bi.apply(fileFormat.getStorage(), data);

        if (content == null) {
            return null;
        }
        FileTransfer ret = new FileTransfer();
        ret.setContentType(contenttype);
        ret.setData(content);
        ret.setFileName(filename);
        ret.setFileType(format);
        return ret;
    }

    public static void writeFile(FileTransfer fileTransfer, boolean isDownload, int thumb, HttpServletRequest request, HttpServletResponse response, ServletOutputStream out) throws Exception {
        response.setHeader("Pragma", "No-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");

        if (!isDownload) {

            FileTransfer ft = ImgHelper.previewFile(fileTransfer, thumb);
            String contentType = ft.getContentType();
            response.setContentType(contentType);
            byte b[] = ft.getData();
            out.write(b);
        } else {
            String userAgent = request.getHeader("USER-AGENT").toLowerCase();
            response.setHeader("Content-Type", "application/force-download; charset=utf-8");
            String fname = fileTransfer.getFileName();
            String URLEncodedFileName = URLEncoder.encode(fname, "UTF-8");
            String ResultFileName = URLEncodedFileName.replace('+', ' ');
            if (userAgent != null
                    && (userAgent.contains("chrome")
                    || userAgent.contains("msie")
                    || userAgent.contains("trident"))) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + ResultFileName + "\"");

            } else {
                response.setHeader("Content-Disposition", "attachment; filename*=\"utf8'ru-ru'" + ResultFileName + "\"");
            }
            out.write(fileTransfer.getData());
        }
    }
}
