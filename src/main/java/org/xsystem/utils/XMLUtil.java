/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author atimofeev
 */
public class XMLUtil {

    public static XPathExpression getXPathExpression(String path) throws RuntimeException {
        path = path.trim();
        try {
            XPath xp = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xp.compile(path);
            return expr;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    //XMLUtil.getNogeList(String path,Node node)
    public static NodeList getNogeList(String path, Node node) throws RuntimeException {
        path = path.trim();
        try {
            XPath xp = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xp.compile(path);

            NodeList lst = (NodeList) expr.evaluate(node, XPathConstants.NODESET);
            return lst;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Node getNoge(String path, Node node) throws RuntimeException {
        path = path.trim();
        try {
            XPath xp = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xp.compile(path);

            Node lst = (Node) expr.evaluate(node, XPathConstants.NODE);
            return lst;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static synchronized String evaluate(String path, Node node) throws RuntimeException {
        path = path.trim();
        try {
            XPath xp = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xp.compile(path);
            String ret = expr.evaluate(node);

            return ret;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /* XMLUtil.evaluate(String path,Node node)
    XPathExpression executerExpr = getXPathExpression("//executer");
        NodeList lst = (NodeList) executerExpr.evaluate(rootDocument, XPathConstants.NODESET);
    
    new PrintWriter(new OutputStreamWriter(
    csocket.getOutputStream(), StandardCharsets.UTF_8), true)
    
     */
    public static void xmlToStream(Node n, OutputStream os) throws Exception {
        Source source = new DOMSource(n);

        //  PrintWriter pr = new PrintWriter(new OutputStreamWriter(os,StandardCharsets.UTF_8),true);
        Result result = new StreamResult(os);//pr);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(source, result);
    }

    public static void xmlToStreamE(Node n, OutputStream os) {
        try {
            Source source = new DOMSource(n);

            //  PrintWriter pr = new PrintWriter(new OutputStreamWriter(os,StandardCharsets.UTF_8),true);
            Result result = new StreamResult(os);//pr);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, result);
        } catch (IllegalArgumentException | TransformerException e) {
            throw new Error(e);
        }
    }

    public static void xmlToStream(Node n, Writer writer) throws Exception {
        Source source = new DOMSource(n);

        //  PrintWriter pr = new PrintWriter(new OutputStreamWriter(os,StandardCharsets.UTF_8),true);
        Result result = new StreamResult(writer);//pr);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(source, result);
    }

    public static String toString(Node element) throws Exception {
        if (element == null) {
            return "null";
        }
        Source source = new DOMSource(element);

        StringWriter stringWriter = new StringWriter();
        try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
            Result result = new StreamResult(printWriter);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, result);
        }
        return stringWriter.toString();
    }

    public static String toStringE(Node element) {
        try {
            if (element == null) {
                return "null";
            }
            Source source = new DOMSource(element);

            StringWriter stringWriter = new StringWriter();
            try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
                Result result = new StreamResult(printWriter);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.transform(source, result);
            }
            return stringWriter.toString();
        } catch (IllegalArgumentException | TransformerException ex) {
            throw new Error(ex);
        }
    }

    public final static void xmlToFile(Node n, File f) throws Exception {
        OutputStream os = new FileOutputStream(f);
        try {
            xmlToStream(n, os);
        } finally {
            Auxilary.close(os);
        }
    }

    public final static void xmlToFileE(Node n, File f)  {
        try {
            xmlToFile(n, f);
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public final static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        return factory.newDocumentBuilder();
    }

    public final static Document newDocument() throws ParserConfigurationException {
        DocumentBuilder bldr = getDocumentBuilder();
        return bldr.newDocument();
    }

    public final static Document newDocumentE() {
        try {
            DocumentBuilder bldr = getDocumentBuilder();
            return bldr.newDocument();
        } catch (ParserConfigurationException ex) {
            throw new Error(ex);
        }
    }

    public final static Document getDocument(File f) throws ParserConfigurationException, SAXException, IOException {
        InputStream is = new FileInputStream(f);
        try {
            Document d = getDocument(is);
            return d;
        } finally {
            Auxilary.close(is);
        }
    }

    public final static Document getDocumentE(File f) {
        try {
            Document d = getDocument(f);
            return d;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new Error(ex);
        }

    }

    public final static Document getDocumentFromResources(String path) throws ParserConfigurationException, SAXException, IOException {
        InputStream is = Auxilary.loadResource(path);

        try {
            Document ret = getDocument(is);
            return ret;
        } finally {
            is.close();
        }
    }

    public final static Document getDocumentFromResourcesE(String path) {

        try {
            Document ret = getDocumentFromResources(path);
            return ret;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new Error(ex);
        }

    }

    public final static Document getDocumentFromResources(String path, ClassLoader classLoader) throws ParserConfigurationException, SAXException, IOException {
        InputStream is = Auxilary.loadResource(path, classLoader);

        try {
            Document ret = getDocument(is);
            return ret;
        } finally {
            is.close();
        }
    }

    public final static Document getDocumentFromResourcesE(String path, ClassLoader classLoader) {

        try {
            Document ret = getDocumentFromResources(path, classLoader);
            return ret;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new Error(ex);
        }

    }

    public final static Document getDocumentE(InputStream is) {
        try {
            return getDocumentBuilder().parse(is);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new Error(ex);
        }
    }

    public final static Document getDocumentE(Reader is) {
        try {
            DocumentBuilder bldr = getDocumentBuilder();
            InputSource src = new InputSource(is);
            return bldr.parse(src);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new Error(ex);
        }
    }

    public final static Document getDocument(Reader is) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder bldr = getDocumentBuilder();
        InputSource src = new InputSource(is);
        return bldr.parse(src);
    }

    public final static Document getDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        return getDocumentBuilder().parse(is);
    }

    public final static Document getDocument(InputSource src) throws ParserConfigurationException, SAXException, IOException {
        return getDocumentBuilder().parse(src);
    }

    public final static Document getDocument(byte[] b) throws ParserConfigurationException, SAXException, IOException {
        InputStream is = new ByteArrayInputStream(b);
        return getDocumentBuilder().parse(is);
    }

    public final static Document getDocument(String str) throws ParserConfigurationException, SAXException, IOException {
        byte[] b = str.getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(b);
        return getDocumentBuilder().parse(is);
    }

    public final static Document getDocumentE(String str) {
        try {
            byte[] b = str.getBytes("UTF-8");
            InputStream is = new ByteArrayInputStream(b);
            return getDocumentBuilder().parse(is);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new Error(ex);
        }
    }

    public static Element getFirstChildElement(Element element) {
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if ((node instanceof Element)
                    && (element == node.getParentNode())) {
                return (Element) node;
            }
        }
        return null;
    }

    public static List<Element> elements(Element element) {
        List<Element> elements = new ArrayList();
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if ((node instanceof Element)
                    && (element == node.getParentNode())) {
                elements.add((Element) node);
            }
        }
        return elements;
    }

    public static Element createNewElement(Node elem, String tag) {// throws Exception {
        if (elem instanceof Document) {
            Document doc = (Document) elem;
            Element res = doc.createElement(tag);
            elem.appendChild(res);
            return res;
        } else {
            Document doc = elem.getOwnerDocument();
            Element res = doc.createElement(tag);
            elem.appendChild(res);
            return res;
        }
    }

    public static Element createNewTextElement(Node elem, String tag, String value) {// throws Exception {
        Document doc = elem.getOwnerDocument();
        Element res = doc.createElement(tag);
        Text content = doc.createTextNode(value);
        res.appendChild(content);
        elem.appendChild(res);
        return res;
    }

    public static Element createNewCDATAElement(Node elem, String tag, String value) {
        Document doc = elem.getOwnerDocument();
        Element res = doc.createElement(tag);
        CDATASection cdata = doc.createCDATASection(value);
        res.appendChild(cdata);
        elem.appendChild(res);
        return res;
    }

    public static void createNewCDATA(Node elem, String value) {
        Document doc = elem.getOwnerDocument();
        if (value == null) {
            value = "";
        }
        CDATASection cdata = doc.createCDATASection(value);
        elem.appendChild(cdata);
    }

    
    
    public static String getContentText(Element element) {
        StringBuffer buffer = new StringBuffer();
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof CharacterData) {
                CharacterData characterData = (CharacterData) node;
                buffer.append(characterData.getData());
            }
        }
        return buffer.toString();
    }

    
    public static String getContentText(Element root,String path) {
        Element element=(Element) getNoge(path,root);
        if (element==null){
            return null;
        }
        String ret =getContentText(element);
      
        return ret;
    }
    
    /*
    public static Node getNoge(String path, Node node) throws RuntimeException {
        path = path.trim();
        try {
            XPath xp = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xp.compile(path);

            Node lst = (Node) expr.evaluate(node, XPathConstants.NODE);
            return lst;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    */
    
    public static String getStringAttr(Element element, String name) {
        String attr = element.getAttribute(name);
        return attr;
    }

    public static String getStringAttr(Element element, String name, String def) {
        String attr = element.getAttribute(name);
        if (attr.isEmpty()) {
            attr = def;
        }
        return attr;
    }

    public static boolean getBooleanAttr(Element element, String name) {
        String attr = element.getAttribute(name);
        boolean ret = attr.equalsIgnoreCase("true");
        return ret;
    }

    public static Integer getIntegerAttr(Element element, String name) {
        String attr = element.getAttribute(name);
        if (!attr.isEmpty()) {
            Integer ret = Integer.valueOf(attr);
            return ret;
        }
        return null;
    }

    public static void setIntegerAttr(Element element, String name, Integer val) {
        if (val != null) {
            element.setAttribute(name, val.toString());
        }
    }

    public static Integer getIntegerAttr(Element element, String name, int def) {
        String attr = element.getAttribute(name);
        if (!attr.isEmpty()) {
            Integer ret = Integer.valueOf(attr);
            return ret;
        }
        return def;
    }

    public static Double getDoubleAttr(Element element, String name) {
        String attr = element.getAttribute(name);
        if (!attr.isEmpty()) {
            Double ret = Double.valueOf(attr);
            return ret;
        }
        return null;
    }
}
