/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http.impl;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;

import org.xsystem.utils.FileTransfer;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author atimofeev
 */
public class ImgHelper {

    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // Controllo se e' necessario eseguire lo scaling     
        if (original_width > bound_width) {
            //scaling della larghezza in base alla larghezza massima        
            new_width = bound_width;
            //eseguo lo scaling dell'altezza per mantenere le proporzioni   
            new_height = (new_width * original_height) / original_width;
        }
        // Dopo aver calcolato la nuova altezza, controllo se Р вЂњР Рѓ ancora fuori limite     
        if (new_height > bound_height) {
            new_height = bound_height;
            //rieseguo lo scaling per mantenere le proporzioni         
            new_width = (new_height * original_width) / original_height;
        }
        return new Dimension(new_width, new_height);
    }
/*
    public static void resaize(String format, int IMG_SIZE, InputStream sorce, OutputStream target) throws IOException {
        BufferedImage originalImage = ImageIO.read(sorce);
        int type = format.equalsIgnoreCase("gif") || (originalImage.getType() == 0)
                ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        Dimension new_dim = //new Dimension(IMG_SIZE, IMG_SIZE);
                getScaledDimension(new Dimension(originalImage.getWidth(), originalImage.getHeight()),
                new Dimension(IMG_SIZE, IMG_SIZE));
        BufferedImage resizedImage = new BufferedImage((int) new_dim.getWidth(), (int) new_dim.getHeight(), type);
        Graphics2D g2 = resizedImage.createGraphics();
        g2.drawImage(originalImage, 0, 0, (int) new_dim.getWidth(), (int) new_dim.getHeight(), null);
        g2.dispose();
        ImageIO.write(resizedImage, format, target);
    }
*/
    
    public static void resaize(String format, int IMG_SIZE, InputStream sorce, OutputStream target) throws IOException {
        BufferedImage originalImage = ImageIO.read(sorce);
        int type = format.equalsIgnoreCase("gif") || (originalImage.getType() == 0)
                ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        Dimension new_dim=null;
        if(IMG_SIZE< originalImage.getWidth()||IMG_SIZE<originalImage.getHeight()){
                 new_dim = //new Dimension(IMG_SIZE, IMG_SIZE);
                getScaledDimension(new Dimension(originalImage.getWidth(), originalImage.getHeight()),
                new Dimension(originalImage.getWidth(),originalImage.getHeight()));
            
        } else {
            new_dim = //new Dimension(IMG_SIZE, IMG_SIZE);
                getScaledDimension(new Dimension(originalImage.getWidth(), originalImage.getHeight()),
                new Dimension(IMG_SIZE, IMG_SIZE));
        }
        BufferedImage resizedImage = new BufferedImage((int) new_dim.getWidth(), (int) new_dim.getHeight(), type);
        Graphics2D g2 = resizedImage.createGraphics();
        g2.drawImage(originalImage, 0, 0, (int) new_dim.getWidth(), (int) new_dim.getHeight(), null);
        g2.dispose();
        ImageIO.write(resizedImage, format, target);
    }
    
    
    public static FileTransfer resaize(int IMG_SIZE, FileTransfer src) throws IOException {

        String srcContentType = src.getContentType();
        String srcFileName = src.getFileName();
        String srcFileType = src.getFileType();
        byte srcData[] = src.getData();
        ByteArrayInputStream is = new ByteArrayInputStream(srcData);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        resaize(srcFileType, IMG_SIZE, is, os);

        FileTransfer ret = new FileTransfer();
        byte retData[] = os.toByteArray();
        ret.setData(retData);
        ret.setContentType(srcContentType);
        ret.setFileName(srcFileName);
        ret.setFileType(srcFileType);
        return ret;
    }

    public static boolean isImg(String format) {
        String[] formatNames = ImageIO.getReaderFormatNames();
        for (String name : formatNames) {
            if (name.equalsIgnoreCase(format)) {
                return true;
            }
        }
        return false;
    }

    public static FileTransfer getPreviewImg(String contentType) throws Exception {
        String base = "files/";
        String fileStub = "file.png";
        switch (contentType) {
            case "application/pdf":
                fileStub = "pdf.png";
                break;
            case "application/msword":
                fileStub = "word.png";
                break;
            case "application/vnd.ms-excel":
                fileStub = "excel.png";
                break;
        }
        String path = base + fileStub;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(path);
        byte retData[];

        retData = IOUtils.toByteArray(stream);

        String resContentType = "image/png";
        String resFileType = "png";
        FileTransfer ret = new FileTransfer();
        ret.setFileName(fileStub);
        ret.setFileType(resFileType);
        ret.setContentType(resContentType);
        ret.setData(retData);

        return ret;
    }

    public static String exctractFormat(FileTransfer src) {
        if (src.getFileType() != null) {
            return src.getFileType();
        }
        String filename = src.getFileName();
        if (filename != null) {
            return Auxilary.getFileExtention(filename);
        }
        return "";

    }

    
    
    
    public static FileTransfer previewFile(FileTransfer src, int IMG_SIZE) throws Exception {
        String format = exctractFormat(src);
        src.setFileType(format);
        if (isImg(src.getFileType())) {
            FileTransfer ret = resaize(IMG_SIZE, src);
            return ret;
        } else {
            src = getPreviewImg(src.getContentType());
            FileTransfer ret = resaize(IMG_SIZE, src);
            return ret;
        }
    }
}
