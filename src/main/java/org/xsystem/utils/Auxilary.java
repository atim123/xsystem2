/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author atimofeev
 */
public class Auxilary {

    static Pattern emailPattern = Pattern.compile("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$");

    public static void XXX(Object o) {
        Object x = o;
    }

    public static boolean isEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        boolean ret = matcher.find();
        return ret;
    }

    public static String toUTF8String(byte[] content) {
        try {
            String ret = new String(content, "UTF-8");
            return ret;
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    ;
    
    public static String readFile(File f) throws IOException {
        String ret;
        FileInputStream fs = new FileInputStream(f);
        try {
            ret = IOUtils.toString(fs, "UTF-8");

        } finally {
            Auxilary.close(fs);
        }
        return ret;
    }

    public static String readFileE(File f) {
        String ret;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            ret = IOUtils.toString(fs, "UTF-8");
        } catch (IOException ex) {
            throw new Error(ex);
        } finally {
            Auxilary.close(fs);
        }
        return ret;
    }

    public static String readStringStreamE(InputStream fs) {
        String ret;
        try {
            ret = IOUtils.toString(fs, "UTF-8");
        } catch (IOException ex) {
            throw new Error(ex);
        }
        return ret;
    }

    public static byte[] readStreamE(InputStream fs) {
        byte[] ret;
        try {
            ret = IOUtils.toByteArray(fs);
        } catch (IOException ex) {
            throw new Error(ex);
        }
        return ret;
    }

    public static byte[] readBinaryFile(File f) throws IOException {
        byte[] ret;
        FileInputStream fs = new FileInputStream(f);
        try {
            ret = IOUtils.toByteArray(fs);

        } finally {
            Auxilary.close(fs);
        }
        return ret;
    }

    public static void copyFile(File srcFile, File destFile) {
        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    public static void moveFile(File srcFile, File destFile) {
        try {
            FileUtils.moveFile(srcFile, destFile);
        } catch (IOException ex) {
            throw new Error(ex);
        }

    }

    public static void writeBinaryFile(File f, byte[] content) throws FileNotFoundException, IOException {
        FileOutputStream fs = new FileOutputStream(f);
        try {
            IOUtils.write(content, fs);
        } finally {
            Auxilary.close(fs);
        }
    }

    public static void writeBinaryFileE(File f, byte[] content) {
        try {
            writeBinaryFile(f, content);
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    public static void writeString(File f, String content) throws FileNotFoundException, IOException {
        FileOutputStream fs = new FileOutputStream(f);
        try {
            IOUtils.write(content, fs, "UTF-8");
        } finally {
            Auxilary.close(fs);
        }
    }

    public static void writeStringE(File f, String content) {

        try {
            writeString(f, content);
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    public static void writeBinaryFile(File f, FileTransfer ft) {
        try {
            byte[] content = ft.getData();
            writeBinaryFile(f, content);
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    public static String createGuid() {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString;
    }

    public static boolean isGuid(String uuid) {
        if (uuid == null) {
            return false;
        }
        try {
            // we have to convert to object and back to string because the built in fromString does not have 
            // good validation logic.
            UUID fromStringUUID = UUID.fromString(uuid);
            String toStringUUID = fromStringUUID.toString();
            return toStringUUID.equals(uuid);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static Map newMap(Object... args) {
        Map res = new LinkedHashMap();
        if (args == null) {
            return res;
        }
        int size = args.length / 2;
        for (int i = 0; i < size; i++) {
            int idx = 2 * i;
            Object key = args[idx];
            Object value = args[idx + 1];
            res.put(key, value);
        }
        return res;
    }

    public static List newList(Object... args) {
        List res = new ArrayList();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                res.add(args[i]);
            }
        }
        return res;
    }

    public static String[] newStringArray(String... args) {
        String[] ret = new String[args.length];
        System.arraycopy(args, 0, ret, 0, args.length);

        return ret;
    }

    public static <T> T getJndiResource(String path, Class<T> type) {
        try {
            InitialContext cxt = new InitialContext();

            Object ret = cxt.lookup(path);

            return (T) ret;
        } catch (NamingException ex) {
            throw new Error(ex);
        }
    }

    public static java.sql.Connection getJndiJDBConnection(String path) {
        try {
            javax.sql.DataSource ds = getJndiResource(path, javax.sql.DataSource.class);
            return ds.getConnection();
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }

     public static void copyResourceToFile(String src,File tgt){
         InputStream is=loadResource(src);
         
        try {
            FileUtils.copyInputStreamToFile(is, tgt);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
     }
    
    public static InputStream loadResource(String path) {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(path);
        return stream;
    }

    public static InputStream loadResource(String path, ClassLoader classLoader) {

        InputStream stream = classLoader.getResourceAsStream(path);
        return stream;
    }

    public static String getResourceAsString(String resurce) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(resurce);
        try {
            String template = IOUtils.toString(stream, "UTF-8");
            return template;
        } finally {
            Auxilary.close(stream);
        }
    }

    public static String getResourceAsStringE(String resurce) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(resurce);
        try {
            String template = IOUtils.toString(stream, "UTF-8");
            return template;
        } catch (IOException ex) {
            throw new Error(ex);
        } finally {
            Auxilary.close(stream);
        }
    }

    public static Properties getPropertiesFromResourceE(String resurce) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(resurce);
        Properties property = new Properties();
        try {
            property.load(stream);
            return property;
        } catch (IOException ex) {
            throw new Error(ex);
        } finally {
            Auxilary.close(stream);
        }
    }

    public static String getErrMsg(Throwable e) {
        while (e != null) {
            String msg = e.getMessage();
            if (msg != null) {
                return msg;
            }
            e = e.getCause();
        }
        return "СИСТЕМНАЯ ОШИБКА";
    }

    public static String throwableToString(Throwable t) {
        OutputStream out = new ByteArrayOutputStream();
        try {
            PrintStream strm = new PrintStream(out);
            try {
                t.printStackTrace(strm);
                String rez = out.toString();//strm.toString();
                return rez;
            } finally {
                try {
                    strm.close();
                } catch (Exception ex) {
                };
            }
        } finally {
            try {
                out.close();
            } catch (Exception ex) {
            };
        }
    }

    public static Map makeJsonSuccess() {
        return makeJsonSuccess(null, null);
    }

    public static String makeJsonSuccessAsString(String resultName, String jsonRes) {
        char q = '"';
        String json = "{" + q + "success" + q + ":true,"
                + q + resultName + q + ":" + jsonRes
                + "}";
        return json;
    }

    public static String makeJsonSuccessAsString() {
        char q = '"';
        String json = "{" + q + "success" + q + ":true}";
        return json;
    }

    public static Map makeJsonSuccess(String resultName, Object result) {
        Map ret = new LinkedHashMap();
        ret.put("success", new Boolean(true));

        if (resultName != null) {
            ret.put(resultName, result);
        }
        return ret;
    }

    public static Map makeJsonSuccessMap(Map<String, Object> responce) {
        Map ret = new LinkedHashMap();
        ret.put("success", true);
        if (responce != null) {
            Iterator<String> itr = responce.keySet().iterator();
            while (itr.hasNext()) {
                String key = itr.next();
                ret.put(key, responce.get(key));
            }
        }
        return ret;
    }

    public static Map makeJsonSuccess(Object result) {
        Map ret = new LinkedHashMap();
        ret.put("success", true);
        if (result != null) {
            ret.put("success", result);
        } else {
            ret.put("success", "true");
        }
        return ret;
    }

    public static Map makeJsonError(String msg, Integer code) {
        Map ret = new LinkedHashMap();
        ret.put("success", new Boolean(false));
        Map error = new LinkedHashMap();
        error.put("message", msg);
        error.put("code", code);
        ret.put("error", error);
        return ret;
    }

    public static Map makeJsonThrowable(Throwable e) {
        Map ret = new LinkedHashMap();
        ret.put("success", new Boolean(false));
        ret.put("error", e.getMessage());
        return ret;
    }

    public static Map makeJsonError(String msg) {
        Map ret = new LinkedHashMap();
        ret.put("success", new Boolean(false));
        ret.put("error", msg);
        return ret;
    }

    public static Map makeJsonError(String msg, Integer code, Throwable e) {
        Map ret = new LinkedHashMap();
        ret.put("success", new Boolean(false));
        Map error = new LinkedHashMap();
        error.put("message", msg);
        error.put("code", code);
        ret.put("error", error);
        return ret;
    }

    public static void stringOut(String src, OutputStream out) throws IOException {
        if (src != null && !src.isEmpty()) {
            out.write(src.getBytes("utf-8"));
        }
    }

    public static String getSubstr(String src, String start, String end) {
        int idx = src.indexOf(start);
        if (idx < 0) {
            throw new Error("getSubstr - invalid template");
        }
        idx = idx + start.length();
        String tgt = src.substring(idx);
        idx = tgt.indexOf(end);
        if (idx < 0) {
            throw new Error("getSubstr - invalid template");
        }
        tgt = tgt.substring(0, idx);
        tgt = tgt.trim();
        return tgt;
    }

    public static List<String> splitNames(String names) {
        if (names != null) {
            String[] namesArray = names.split("\\s*,\\s*");
            List<String> ret = Arrays.asList(namesArray);
            return ret;
        }
        return null;
    }

    public static boolean isDate(String str) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            df.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static Date toDate(String str) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date d = df.parse(str);
            return d;
        } catch (ParseException e) {
            return null;
        }
    }

    public static String toString(Date d, String format) {
        if (d == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    public static Date toDate(String d, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(d);
    }

    public static Integer toInt(String s) {
        try {
            Integer ret = Integer.parseInt(s);
            return ret;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static Integer toInt2(Object s) throws NumberFormatException {
        if (s == null) {
            return null;
        }
        if (s instanceof String) {
            Integer ret = Integer.parseInt((String) s);
            return ret;
        } else if (s instanceof Double) {
            Double d = (Double) s;
            Integer ret = d.intValue();
            return ret;
        } else if (s instanceof Integer) {
            return (Integer) s;
        } else if (s instanceof Long) {
            Long l = (Long) s;
            Integer ret = l.intValue();
            return ret;
        }
        throw new NumberFormatException();
    }

    public static BigDecimal toBigDecimal(String s) {
        try {
            BigDecimal d = new BigDecimal(s);
            return d;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static Boolean toBooolean(String s) {
        if (s.equals("true")) {
            return new Boolean(true);
        } else if (s.equals("false")) {
            return new Boolean(false);
        } else {
            return null;
        }
    }

    public static boolean isInteger(Double variable) {
        boolean ret = ((variable == Math.floor(variable)) && !Double.isInfinite(variable));
        return ret;
    }

    public static String getPathExtention(String filename) {
        int pathPos = filename.lastIndexOf(".");
        if (pathPos == -1) {
            return filename;
        }
        String ret = filename.substring(0, pathPos - 2);
        return ret;
    }

    public static String getPathWithoutExtention(String filename) {
        int pathPos = filename.lastIndexOf(".");
        if (pathPos == -1) {
            return filename;
        }
        String ret = filename.substring(0, pathPos);
        return ret;
    }

    public static String getFileExtention(String filename) {
        int dotPos = filename.lastIndexOf(".") + 1;
        if (dotPos == 0) {
            return "";
        }
        return filename.substring(dotPos);
    }

    public static boolean getAutoComit(Connection cn) {
        try {
            boolean ret = cn.getAutoCommit();
            return ret;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void setAutoCommit(Connection cn, boolean prm) {
        try {
            boolean ret = cn.getAutoCommit();
            cn.setAutoCommit(prm);
        } catch (SQLException ex) {
        }
    }

    public static void commit(Connection cn) {
        try {
            cn.commit();
        } catch (SQLException ex) {
        }
    }

    public static void rollback(Connection cn) {
        try {
            cn.rollback();
        } catch (SQLException ex) {
        }
    }

    public static Connection close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        return null;
    }

    public static ResultSet close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        return null;
    }

    public static PreparedStatement close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        return null;
    }

    public static PreparedStatement close(Statement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        return null;
    }

    public static Closeable close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return null;
    }

    public static XMLStreamWriter close(XMLStreamWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        };
        return null;
    }

    public static Boolean isEmptyOrNull(String v) {
        if (v == null) {
            return true;
        }
        boolean ret = v.isEmpty();
        return ret;
    }

    public static Boolean isEmptyOrNull(Collection v) {
        if (v == null) {
            return true;
        }
        boolean ret = v.isEmpty();
        return ret;
    }

    public static boolean buildDir(String path) {
        File f = new File(path);
        if (f.exists()) {
            if (f.isDirectory()) {
                return true;
            } else {
                return false;
            }

        }
        boolean result = f.mkdirs();
        return result;
    }

    public static boolean buildDir(File f) {
         if (f.exists()) {
            if (f.isDirectory()) {
                return true;
            } else {
                return false;
            }

        }
        boolean result = f.mkdirs();
        return result;
    }
    
    public static boolean cleanDirectory(String directory) {
        return cleanDirectory(new File(directory));
    }
    
    public static boolean cleanDirectory(File directory) {
        try {
            FileUtils.cleanDirectory(directory);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static void cleanDirectoryE(String directory) {
        cleanDirectoryE(new File(directory));
    }
    
    public static void cleanDirectoryE(File directory) {
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }
    
    public static boolean deleteDirectory(String directory){
        return deleteDirectory(new File(directory));
    }
    
    public static boolean deleteDirectory(File directory){
    try {
            FileUtils.deleteDirectory(directory);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
    public static void deleteDirectoryE(String directory){
        deleteDirectory(new File(directory));
    }
    
    public static void deleteDirectoryE(File directory){
    try {
            FileUtils.deleteDirectory(directory);
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }
    
    public static boolean buildDir(String root, String path) {
        File rf = new File(root);
        if (rf.exists()) {
            if (rf.isDirectory()) {
                File f = new File(rf, path);
                if (f.exists()) {
                    if (f.isDirectory()) {
                        return true;
                    }
                } else {
                    boolean result = f.mkdirs();
                    return result;
                }
            } else {
                return false;
            }

        }
        boolean result = rf.mkdirs();
        if (result) {
            File f = new File(rf, path);
            result = f.mkdirs();
        }
        return result;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String numberToString(Double dd) {
        if (dd == null) {
            return null;
        }
        String ret = new BigDecimal(dd).toPlainString();
        return ret;
    }

    public static String numberToString(Float dd) {
        if (dd == null) {
            return null;
        }
        String ret = new BigDecimal(dd).toPlainString();

        return ret;
    }

    public static String md5Hash(String st) {
        String md5Hex = DigestUtils.md5Hex(st);
        return md5Hex;
    }

    public static void putValue(Map tgt, String key, Object value) {
        if (tgt != null && key != null && value != null) {
            tgt.put(key, value);
        }
    }

    public static byte[] iconv(byte[] b, String oldcharset, String newcharset) {
        try {
            String str = new String(b, oldcharset);
            byte[] ret = str.getBytes(newcharset);
            return ret;
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex);

        }
    }

    public static String iconv(String src, String oldcharset, String newcharset) {
        try {
            byte[] b = src.getBytes(oldcharset);
            String ret = new String(b, newcharset);
            return ret;
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex);

        }
    }

    public static Date now() {
        long t = System.currentTimeMillis();
        Date d = new Date(t);
        return d;
    }

    public static final int MISINNH = 1000 * 60 * 60;
    public static final int MISINM = 1000 * 60;
    public static final int MISINS = 1000;

    public static String duration(Date start) {
        Long s = start.getTime();
        long e = now().getTime();
        long d = e - s;

        return duration(d);
    }

    public static String duration(long d) {
        long H = d / MISINNH;
        long M = (d - H * MISINNH) / MISINM;
        long S = (d - H * MISINNH - M * MISINM) / MISINS;

        String ret = "" + H + ":" + M + ":" + S;

        return ret;
    }

    public static boolean checkDate(String value, String format) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            return false;
        };
        if (date == null) {
            return false;
        } else {
            return true;
        }
        // Valid date format
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            throw new Error(ex);
        }
    }
}
/*

Calendar CurrentData = Calendar.getInstance();

        
        Calendar calculate = Calendar.getInstance();
        calculate.setTime(new Date(CurrentData.getTime().getTime() - SaveData.getTime().getTime()));

        
        System.out.println(((calculate.get(Calendar.DAY_OF_YEAR) - 1 ) * 24)  + (calculate.get(Calendar.HOUR_OF_DAY) - 11) + ":" + 
                calculate.get(Calendar.MINUTE) + ":" + calculate.get(Calendar.SECOND));



long em = (long)((System.currentTimeMillis()-lp) / 1000 / 60L);//min ::
int eh = (int)((em*60)-(int)(em/60));//hour ::
int ed = (int)((em*60*24)-(int)(em/60/24))
 */
