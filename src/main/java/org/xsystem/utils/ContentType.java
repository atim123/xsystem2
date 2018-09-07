/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author atimofeev
 */
public class ContentType {

    static Map typeContent = new HashMap();

    public static String extToContentType(String ext) {
        String ct = (String) typeContent.get(ext);
        if (ct == null) {
            ct = "application/octet-stream";
        }
        return ct;
    }

    static {
        typeContent = new HashMap();
        typeContent.put("context", "text/directory");
        typeContent.put("groupmngrlink", "application/octet-stream");
        typeContent.put("directory", "text/directory");
        typeContent.put("xml", "text/xml");
        typeContent.put("jrxm", "text/xml");
        typeContent.put("323", "text/h323");
        typeContent.put("acx", "application/internet-property-stream");
        typeContent.put("ai", "application/postscript");
        typeContent.put("aif", "audio/x-aiff");
        typeContent.put("aifc", "audio/x-aiff");
        typeContent.put("aiff", "audio/x-aiff");
        typeContent.put("asf", "video/x-ms-asf");
        typeContent.put("asr", "video/x-ms-asf");
        typeContent.put("asx", "video/x-ms-asf");
        typeContent.put("au", "audio/basic");
        typeContent.put("avi", "video/x-msvideo");
        typeContent.put("axs", "application/olescript");
        typeContent.put("bas", "text/plain");
        typeContent.put("bcpio", "application/x-bcpio");
        typeContent.put("bin", "application/octet-stream");
        typeContent.put("bmp", "image/bmp");
        typeContent.put("c", "text/plain");
        typeContent.put("cat", "application/vnd.ms-pkiseccat");
        typeContent.put("cdf", "application/x-cdf");
        typeContent.put("cer", "application/x-x509-ca-cert");
        typeContent.put("class", "application/octet-stream");
        typeContent.put("clp", "application/x-msclip");
        typeContent.put("cmx", "image/x-cmx");
        typeContent.put("cod", "image/cis-cod");
        typeContent.put("cpio", "application/x-cpio");
        typeContent.put("crd", "application/x-mscardfile");
        typeContent.put("crl", "application/pkix-crl");
        typeContent.put("crt", "application/x-x509-ca-cert");
        typeContent.put("csh", "application/x-csh");
        typeContent.put("css", "text/css");
        typeContent.put("dcr", "application/x-director");
        typeContent.put("der", "application/x-x509-ca-cert");
        typeContent.put("dir", "application/x-director");
        typeContent.put("dll", "application/x-msdownload");
        typeContent.put("dms", "application/octet-stream");
        typeContent.put("doc", "application/msword");
        typeContent.put("dot", "application/msword");
        typeContent.put("dvi", "application/x-dvi");
        typeContent.put("dxr", "application/x-director");
        typeContent.put("eps", "application/postscript");
        typeContent.put("etx", "text/x-setext");
        typeContent.put("evy", "application/envoy");
        typeContent.put("exe", "application/octet-stream");
        typeContent.put("fif", "application/fractals");
        typeContent.put("flr", "x-world/x-vrml");
        typeContent.put("gif", "image/gif");
        typeContent.put("gtar", "application/x-gtar");
        typeContent.put("gz", "application/x-gzip");
        typeContent.put("h", "text/plain");
        typeContent.put("hdf", "application/x-hdf");
        typeContent.put("hlp", "application/winhlp");
        typeContent.put("hqx", "application/mac-binhex40");
        typeContent.put("hta", "application/hta");
        typeContent.put("htc", "text/x-component");
        typeContent.put("htm", "text/html");
        typeContent.put("html", "text/html");
        typeContent.put("htt", "text/webviewhtml");
        typeContent.put("ico", "image/x-icon");
        typeContent.put("ief", "image/ief");
        typeContent.put("iii", "application/x-iphone");
        typeContent.put("ins", "application/x-internet-signup");
        typeContent.put("isp", "application/x-internet-signup");
        typeContent.put("jfif", "image/pipeg");
        typeContent.put("jpe", "image/jpeg");
        typeContent.put("jpeg", "image/jpeg");
        typeContent.put("jpg", "image/jpeg");
        typeContent.put("js", "application/x-javascript");
        typeContent.put("json", "application/json");
        typeContent.put("latex", "application/x-latex");
        typeContent.put("lha", "application/octet-stream");
        typeContent.put("lsf", "video/x-la-asf");
        typeContent.put("lsx", "video/x-la-asf");
        typeContent.put("lzh", "application/octet-stream");
        typeContent.put("m13", "application/x-msmediaview");
        typeContent.put("m14", "application/x-msmediaview");
        typeContent.put("m3u", "audio/x-mpegurl");
        typeContent.put("man", "application/x-troff-man");
        typeContent.put("mdb", "application/x-msaccess");
        typeContent.put("me", "application/x-troff-me");
        typeContent.put("mht", "message/rfc822");
        typeContent.put("mhtml", "message/rfc822");
        typeContent.put("mid", "audio/mid");
        typeContent.put("mny", "application/x-msmoney");
        typeContent.put("mov", "video/quicktime");
        typeContent.put("movie", "video/x-sgi-movie");
        typeContent.put("mp2", "video/mpeg");
        typeContent.put("mp3", "audio/mpeg");
        typeContent.put("mpa", "video/mpeg");
        typeContent.put("mpe", "video/mpeg");
        typeContent.put("mpeg", "video/mpeg");
        typeContent.put("mpg", "video/mpeg");
        typeContent.put("mpp", "application/vnd.ms-project");
        typeContent.put("mpv2", "video/mpeg");
        typeContent.put("ms", "application/x-troff-ms");
        typeContent.put("msg", "application/vnd.ms-outlook");
        typeContent.put("mvb", "application/x-msmediaview");
        typeContent.put("nc", "application/x-netcdf");
        typeContent.put("nws", "message/rfc822");
        typeContent.put("oda", "application/oda");
        typeContent.put("p10", "application/pkcs10");
        typeContent.put("p12", "application/x-pkcs12");
        typeContent.put("p7b", "application/x-pkcs7-certificates");
        typeContent.put("p7c", "application/x-pkcs7-mime");
        typeContent.put("p7m", "application/x-pkcs7-mime");
        typeContent.put("p7r", "application/x-pkcs7-certreqresp");
        typeContent.put("p7s", "application/x-pkcs7-signature");
        typeContent.put("pbm", "image/x-portable-bitmap");
        typeContent.put("pdf", "application/pdf");
        typeContent.put("pfx", "application/x-pkcs12");
        typeContent.put("pgm", "image/x-portable-graymap");
        typeContent.put("pko", "application/ynd.ms-pkipko");
        typeContent.put("pma", "application/x-perfmon");
        typeContent.put("pmc", "application/x-perfmon");
        typeContent.put("pml", "application/x-perfmon");
        typeContent.put("pmr", "application/x-perfmon");
        typeContent.put("pmw", "application/x-perfmon");
        typeContent.put("pnm", "image/x-portable-anymap");
        typeContent.put("pot", "application/vnd.ms-powerpoint");
        typeContent.put("ppm", "image/x-portable-pixmap");
        typeContent.put("pps", "application/vnd.ms-powerpoint");
        typeContent.put("ppt", "application/vnd.ms-powerpoint");
        typeContent.put("prf", "application/pics-rules");
        typeContent.put("ps", "application/postscript");
        typeContent.put("pub", "application/x-mspublisher");
        typeContent.put("qt", "video/quicktime");
        typeContent.put("ra", "audio/x-pn-realaudio");
        typeContent.put("ram", "audio/x-pn-realaudio");
        typeContent.put("ras", "image/x-cmu-raster");
        typeContent.put("rgb", "image/x-rgb");
        typeContent.put("rmi", "audio/mid");
        typeContent.put("roff", "application/x-troff");
        typeContent.put("rtf", "application/rtf");
        typeContent.put("rtx", "text/richtext");
        typeContent.put("scd", "application/x-msschedule");
        typeContent.put("sct", "text/scriptlet");
        typeContent.put("setpay", "application/set-payment-initiation");
        typeContent.put("setreg", "application/set-registration-initiation");
        typeContent.put("sh", "application/x-sh");
        typeContent.put("shar", "application/x-shar");
        typeContent.put("sit", "application/x-stuffit");
        typeContent.put("snd", "audio/basic");
        typeContent.put("spc", "application/x-pkcs7-certificates");
        typeContent.put("spl", "application/futuresplash");
        typeContent.put("src", "application/x-wais-source");
        typeContent.put("sst", "application/vnd.ms-pkicertstore");
        typeContent.put("stl", "application/vnd.ms-pkistl");
        typeContent.put("stm", "text/html");
        typeContent.put("sv4cpio", "application/x-sv4cpio");
        typeContent.put("sv4crc", "application/x-sv4crc");
        typeContent.put("svg", "image/svg+xml");
        typeContent.put("swf", "application/x-shockwave-flash");
        typeContent.put("t", "application/x-troff");
        typeContent.put("tar", "application/x-tar");
        typeContent.put("tcl", "application/x-tcl");
        typeContent.put("tex", "application/x-tex");
        typeContent.put("texi", "application/x-texinfo");
        typeContent.put("texinfo", "application/x-texinfo");
        typeContent.put("tgz", "application/x-compressed");
        typeContent.put("tif", "image/tiff");
        typeContent.put("tiff", "image/tiff");
        typeContent.put("tr", "application/x-troff");
        typeContent.put("trm", "application/x-msterminal");
        typeContent.put("tsv", "text/tab-separated-values");
        typeContent.put("txt", "text/plain");
        typeContent.put("uls", "text/iuls");
        typeContent.put("ustar", "application/x-ustar");
        typeContent.put("vcf", "text/x-vcard");
        typeContent.put("vrml", "x-world/x-vrml");
        typeContent.put("wav", "audio/x-wav");
        typeContent.put("wcm", "application/vnd.ms-works");
        typeContent.put("wdb", "application/vnd.ms-works");
        typeContent.put("wks", "application/vnd.ms-works");
        typeContent.put("wmf", "application/x-msmetafile");
        typeContent.put("wps", "application/vnd.ms-works");
        typeContent.put("wri", "application/x-mswrite");
        typeContent.put("wrl", "x-world/x-vrml");
        typeContent.put("wrz", "x-world/x-vrml");
        typeContent.put("xaf", "x-world/x-vrml");
        typeContent.put("xbm", "image/x-xbitmap");
        typeContent.put("xla", "application/vnd.ms-excel");
        typeContent.put("xlc", "application/vnd.ms-excel");
        typeContent.put("xlm", "application/vnd.ms-excel");
        typeContent.put("xls", "application/vnd.ms-excel");
        typeContent.put("xlt", "application/vnd.ms-excel");
        typeContent.put("xlw", "application/vnd.ms-excel");
        typeContent.put("xof", "x-world/x-vrml");
        typeContent.put("xpm", "image/x-xpixmap");
        typeContent.put("xwd", "image/x-xwindowdump");
        typeContent.put("z", "application/x-compress");
        typeContent.put("zip", "application/zip");
        typeContent.put("jar", "application/java-archive");
    }
}
