/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.FileTransfer;

/**
 *
 * @author atimofeev
 */
public class TemporaryFileService extends RestApiTemplate {
    
    public static String attrTemporaryFileContext = "org.xsystem.http.TemporaryFileService";
    
    Pattern createPattern = Pattern.compile("^\\/create");
    Pattern deletePattern = Pattern.compile("^\\/delete\\/(.+)");
    
    public static Map<String, FileTransfer> getTemporaryContext(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, FileTransfer> instances = (Map) session.getAttribute(attrTemporaryFileContext);
        if (instances == null) {
            instances = new HashMap();
            session.setAttribute(attrTemporaryFileContext, instances);
        }
        return instances;
    }
    
    public static String createTemporaryFile(HttpServletRequest request) throws FileUploadException, IOException {
        FileTransfer ft = createFileTransfer(request);
        if (ft == null) {
            return null;
        }
        ServletContext scontext = request.getSession().getServletContext();
        scontext.getServletContextName();
        createFile(ft, scontext);
        String guid = Auxilary.createGuid();
        
        Map<String, FileTransfer> instances = getTemporaryContext(request);
        
        instances.put(guid, ft);
        return guid;
    }
    
    public static void destroyTemporaryFile(HttpServletRequest request, String guid) throws UnsupportedEncodingException {
        Map<String, FileTransfer> context = getTemporaryContext(request);
        FileTransfer ft = context.get(guid);
        if (ft != null) {
            context.remove(guid);
            byte[] b = ft.getData();
            if (b != null) {
                String path = new String(b, "UTF-8");
                File f = new File(path);
                f.delete();
            }
        }
    }
    
    static void createFile(FileTransfer ft, ServletContext scontext) throws IOException {
        if (ft != null) {
            File tempDir = (File) scontext.getAttribute("javax.servlet.context.tempdir");
            String prfx = scontext.getServletContextName();
            File tempFile = File.createTempFile(prfx, ".tmp", tempDir);
            byte b[]= ft.getData();
            Auxilary.writeBinaryFile(tempFile, b);
            
            String pth = tempFile.getAbsolutePath();
            ft.setData(pth.getBytes("UTF-8"));
        }
    }
    
    static FileTransfer createFileTransfer(HttpServletRequest request) throws FileUploadException {
        if (ServletFileUpload.isMultipartContent(request)) {
            Map ret = new HashMap();
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            List items = upload.parseRequest(request);
            Iterator iterator = items.iterator();
            while (iterator.hasNext()) {
                FileItem item = (FileItem) iterator.next();
                if (!item.isFormField()) {
                    FileTransfer fileTransfer = new FileTransfer();
                    String fileName = item.getName();
                    String contentType = item.getContentType();
                    byte[] data = item.get();
                    fileTransfer.setFileName(fileName);
                    String ft = Auxilary.getFileExtention(fileName);
                    fileTransfer.setFileType(ft);
                    fileTransfer.setContentType(contentType);
                    fileTransfer.setData(data);
                    return fileTransfer;
                }
            }
            return null;
        } else {
            throw new Error("expected form multipart");
        }
    }
    
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        //response.setHeader("Cache-Control", "no-cache");

        response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        
        String path = request.getPathInfo();
        try (ServletOutputStream out = response.getOutputStream()) {
            try {
                Matcher matcher = Pattern.compile("").matcher(path);
                if (matcher.reset().usePattern(createPattern).find()) {
                    String ret = createTemporaryFile(request);
                    writeJson(Auxilary.makeJsonSuccess("data", ret), out);
                } else if (matcher.reset().usePattern(deletePattern).find()) {
                    String fid = matcher.group(1);
                    destroyTemporaryFile(request, fid);
                    writeJson(Auxilary.makeJsonSuccess(), out);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (Throwable err) {
                error(err, out);
            }
        }
    }
}
