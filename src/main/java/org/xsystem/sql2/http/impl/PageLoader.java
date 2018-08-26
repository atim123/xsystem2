/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xsystem.sql2.http.ActionEventListener;
import org.xsystem.sql2.http.AfterCreateConnectionEventListener;
import org.xsystem.sql2.http.BeforeCloseConnectionEventListener;
import org.xsystem.sql2.http.ConnectionManager;
import org.xsystem.sql2.http.Enviroment;
import org.xsystem.sql2.http.ErrorHandler;
import org.xsystem.sql2.http.Executer;
import org.xsystem.sql2.http.actions.StandartErrorHandler;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author atimofeev
 */
public class PageLoader {

    Document rootDocument;
    Map<String, String> connections = new HashMap();
    Map<String, String> fileStorages = new HashMap();
    
    Map<String, String> objectNames=new HashMap();
    
    
    
    Map<String, AfterCreateConnectionEventListener> afterCreateConnectionEventListeners = new HashMap();

    Map<String, BeforeCloseConnectionEventListener> beforeCloseConnectionEventListeners = new HashMap();

    List<ActionExecuter> actions = new ArrayList();
    Map<String, String> actionsAlias = new HashMap();

    Set<File> fileSet = new HashSet();
    ConnectionManager connectionManager = null;

    Map<String, String> actionEvents = new HashMap();

    ErrorHandler errorHandler = new StandartErrorHandler();

    public Map<String, String> getObjectNames(){
        return objectNames;
    }
    
    public Map<String, String> getConnections() {
        return connections;
    }

    public Map<String, String> getFileStorages() {
        return fileStorages;
    }

    public List<ActionExecuter> getActions() {
        return actions;
    }

    public Set<File> getFileSet() {
        return fileSet;
    }

    public Map<String, AfterCreateConnectionEventListener> getAfterCreateConnectionEventListeners() {
        return afterCreateConnectionEventListeners;
    }

    public Map<String, BeforeCloseConnectionEventListener> getBeforeCloseConnectionEventListeners() {
        return beforeCloseConnectionEventListeners;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void load(String repositoryPathParam) {
        rootDocument = null;
        connectionManager = null;
        File repositoryConfg = new File(repositoryPathParam);
        fileSet.clear();
        fileSet.add(repositoryConfg);
        try {
            rootDocument = XMLUtil.getDocument(repositoryConfg);
            resolveInclude(rootDocument);
            loadErrorHandler();
            loadConnectionManager();
            loadObjectNames();
            loadEvent();
            loadExecuter();
            loadConnections();
            loadFileStorages();
            loadPage();
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex.toString(), ex);
        }
    }

    void resolveInclude(Document doc) throws ParserConfigurationException, SAXException, IOException {
        NodeList lst = XMLUtil.getNogeList("//include", doc);
        Enviroment enviroment = Enviroment.getEnviroment();
        for (int i = 0; i < lst.getLength(); i++) {
            Element element = (Element) lst.item(i);
            String path = element.getAttribute("href");
            if (!Auxilary.isEmptyOrNull(path)) {
                Document includeDocument = null;
                if (Enviroment.isFile(path)) {
                    File f = enviroment.getFile(path);
                    includeDocument = XMLUtil.getDocument(f);
                    if (!fileSet.contains(f)) {
                        fileSet.add(f);
                        resolveInclude(includeDocument);
                    }
                }
                if (includeDocument != null) {
                    Element newElement = includeDocument.getDocumentElement();
                    Element parent = (Element) element.getParentNode();

                    Document self = parent.getOwnerDocument();
                    newElement = (Element) self.importNode(newElement, true);
                    parent.replaceChild(newElement, element);
                }
            }
        }
    }

    Object createObject(String className) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class cls1 = classLoader.loadClass(className);
            Object ret = cls1.newInstance();
            return ret;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            throw new Error(ex);
        }
    }

    void loadConnectionManager() {
        connectionManager = new JNDIConnectionManager();
        Element rootElement = rootDocument.getDocumentElement();
        Attr atr = (Attr) XMLUtil.getNoge("connectionmanager/@class", rootElement);
        if (atr != null) {
            String className = atr.getValue();
            connectionManager = (ConnectionManager) createObject(className);

        }
    }

    void loadEvent() {
        NodeList lst = XMLUtil.getNogeList("//event", rootDocument);
        for (int i = 0; i < lst.getLength(); i++) {
            Element element = (Element) lst.item(i);

            String name = element.getAttribute("name");
            String clazname = element.getAttribute("class");
            if (!Auxilary.isEmptyOrNull(name) && !Auxilary.isEmptyOrNull(clazname)) {
                actionEvents.put(name, clazname);
            }
        }
    }

    void loadErrorHandler() {
        Element elem = (Element) XMLUtil.getNoge("//errorhandler", rootDocument);
        if (elem != null) {
            String className = XMLUtil.getContentText(elem);
            if (!Auxilary.isEmptyOrNull(className)) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                try {
                    Class cls1 = classLoader.loadClass(className);
                    errorHandler = (ErrorHandler) cls1.newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    void loadExecuter() {
        actionsAlias.put("query", "org.xsystem.sql2.http.actions.SelectExecuter");
        actionsAlias.put("row", "org.xsystem.sql2.http.actions.RowExecuter");
        actionsAlias.put("exec", "org.xsystem.sql2.http.actions.ExecExecuter");
        actionsAlias.put("dml", "org.xsystem.sql2.http.actions.DMLExecuter");
        actionsAlias.put("echo", "org.xsystem.sql2.http.actions.EchoExcecuter");
        
        actionsAlias.put("mbeaninfo","org.xsystem.sql2.http.actions.MBeanInfoExecuter");
        actionsAlias.put("mbeaninvoke","org.xsystem.sql2.http.actions.MBeanInvokeExecuter");
        
        NodeList lst = XMLUtil.getNogeList("//executer", rootDocument);

        for (int i = 0; i < lst.getLength(); i++) {
            Element element = (Element) lst.item(i);
            String name = element.getAttribute("name");
            String clazname = element.getAttribute("class");
            if (!Auxilary.isEmptyOrNull(name) && !Auxilary.isEmptyOrNull(clazname)) {
                actionsAlias.put(name, clazname);
            }
        }
    }

    void setConnectionEventListener(Element element) {
        String className = element.getAttribute("eventListener");

        if (!Auxilary.isEmptyOrNull(className)) {
            String key = element.getLocalName();
            Object listener = createObject(className);
            if (listener instanceof AfterCreateConnectionEventListener) {
                AfterCreateConnectionEventListener afterCreateConnectionEventListener = (AfterCreateConnectionEventListener) listener;
                afterCreateConnectionEventListeners.put(key, afterCreateConnectionEventListener);
            }
            if (listener instanceof BeforeCloseConnectionEventListener) {
                BeforeCloseConnectionEventListener beforeCloseConnectionEventListener = (BeforeCloseConnectionEventListener) listener;
                beforeCloseConnectionEventListeners.put(key, beforeCloseConnectionEventListener);
            }
        }

    }

    void loadObjectNames() throws XPathExpressionException {
       Element rootElement = rootDocument.getDocumentElement();
       NodeList lst = XMLUtil.getNogeList("//objectname", rootElement);
       for (int i = 0; i < lst.getLength(); i++) {
            Element element = (Element) lst.item(i);
            String key=element.getAttribute("name");
            String value = XMLUtil.getContentText(element);
            value =value.trim();
            objectNames.put(key, value);
        }
    }
    
    void loadConnections() throws XPathExpressionException {
        Element rootElement = rootDocument.getDocumentElement();
        Element element = (Element) XMLUtil.getNoge("connection", rootElement);
        XMLUtil.elements(element).forEach(chldelement -> {
            String key = chldelement.getLocalName();
            String value = XMLUtil.getContentText(chldelement);
            connections.put(key, value);
            setConnectionEventListener(chldelement);
        });
    }

    void loadFileStorages() throws XPathExpressionException {
        Element rootElement = rootDocument.getDocumentElement();
        Element element = (Element) XMLUtil.getNoge("filestorage", rootElement);
        if (element != null) {
            XMLUtil.elements(element).forEach(chldelement -> {
                String key = chldelement.getLocalName();
                String value = XMLUtil.getContentText(chldelement);
                fileStorages.put(key, value);
                setConnectionEventListener(chldelement);
            });
        }
    }

    void loadPage() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Element rootElement = rootDocument.getDocumentElement();
        NodeList lst = XMLUtil.getNogeList("//action", rootElement);
        for (int i = 0; i < lst.getLength(); i++) {
            Element actionelement = (Element) lst.item(i);
            ActionExecuter action = loadAction(actionelement);
            actions.add(action);
        }
    }

    ActionEventListener buildActionEventListener(String eventName) {
        String className = actionEvents.get(eventName);
        if (Auxilary.isEmptyOrNull(className)) {
            throw new RuntimeException("class from actionEvent " + eventName + " not register");
        }
        if (!Auxilary.isEmptyOrNull(eventName)) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            try {
                Class cls1 = classLoader.loadClass(className);
                ActionEventListener ret = (ActionEventListener) cls1.newInstance();
                return ret;
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        throw new RuntimeException("class from eventName not register");
    }

    ActionEventListener loadActionEventListener(Element element) {
        String eventName = element.getAttribute("name");
        ActionEventListener actionEventListener = buildActionEventListener(eventName);
        return actionEventListener;
    }

    ;
    
    ActionExecuter loadAction(Element element) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ActionExecuter actionExecuter = new ActionExecuter();

        String type = element.getAttribute("type");
        String connectionName = getConnectionName(element);

        if (Auxilary.isEmptyOrNull(connectionName)) {
            connectionName = "defualt";
        }

        actionExecuter.setConnectionName(connectionName);
        String prfx = loadPrefix(element);

        String location = getLocation(element);
        actionExecuter.setLocation(location);

        if (Auxilary.isEmptyOrNull(type)) {
            String ss=XMLUtil.toStringE(element);
            System.out.println(ss);
            throw new RuntimeException("invalid action " + ss);
        }
        
        Executer executer = buildExecuter(type);
        actionExecuter.setAction(executer);

        XMLUtil.elements(element).forEach(chldelement -> {
            String attrName = chldelement.getLocalName();
            switch (attrName) {

                case "before": {
                    ActionEventListener beforeEvent = loadActionEventListener(chldelement);
                    actionExecuter.setBeforeEvent(beforeEvent);
                    break;
                }
                case "after": {
                    ActionEventListener afterEvent = loadActionEventListener(chldelement);
                    actionExecuter.setAfterEvent(afterEvent);
                    break;
                }
                case "patern": {
                    String val = XMLUtil.getContentText(chldelement);
                    val = prfx + val;
                    actionExecuter.setPattern(Pattern.compile(val));
                    break;
                }
                case "context": {
                    String strForm = chldelement.getAttribute("form");
                    String strMultipart =chldelement.getAttribute("multipart");
                    if (!Auxilary.isEmptyOrNull(strForm)) {
                        actionExecuter.setForm(Boolean.parseBoolean(strForm));
                    }
                    if (!Auxilary.isEmptyOrNull(strMultipart)) {
                        actionExecuter.setMultipart(Boolean.parseBoolean(strMultipart));
                    }
                    
                    XMLUtil.elements(chldelement).forEach(ctxelement -> {
                        String key = ctxelement.getLocalName();
                        String eval = XMLUtil.getContentText(ctxelement);
                        actionExecuter.getContextParams().put(key, eval.trim());
                    });
                    break;
                }
                case "file": {
                    FileFormat fileFormat = new FileFormat();
                    String strdownload = chldelement.getAttribute("download");
                    if (!Auxilary.isEmptyOrNull(strdownload)) {
                        fileFormat.setDownload(Boolean.parseBoolean(strdownload));
                    }
                    fileFormat.setPreview(chldelement.getAttribute("preview"));
                    String storage = chldelement.getAttribute("storage");
                    if (!Auxilary.isEmptyOrNull(storage)) {
                        fileFormat.setStorage(storage);
                    }

                    fileFormat.setNotfound(chldelement.getAttribute("notfound"));

                    actionExecuter.setFileFormat(fileFormat);
                    XMLUtil.elements(chldelement).forEach(fileelement -> {
                        String key = fileelement.getLocalName();
                        String value = XMLUtil.getContentText(fileelement);
                        switch (key) {
                            case "contenttype":
                                fileFormat.setContenttype(value);
                                break;
                            case "content":
                                fileFormat.setContent(value);
                                break;
                            case "format":
                                fileFormat.setFormat(value);
                                break;
                            case "filename":
                                fileFormat.setFilename(value);
                                break;
                        }

                    });
                }
            }
        });
        executer.parse(element);
        return actionExecuter;

    }

    String getConnectionName(Element element) {
        String connectionName = element.getAttribute("connection");
        if (Auxilary.isEmptyOrNull(connectionName)) {
            Node parentNode = element.getParentNode();
            if (parentNode instanceof Document) {
                return null;
            }
            return getConnectionName((Element) parentNode);
        }
        return connectionName;
    }

    String getLocation(Element element) {
        String location = XMLUtil.evaluate("location/text()", element);
        if (Auxilary.isEmptyOrNull(location)) {
            Node parentNode = element.getParentNode();
            if (parentNode instanceof Element) {
                Element parentElemet = (Element) parentNode;
                return getLocation(parentElemet);
            }
        }
        return location;
    }

    String loadPrefix(Element element) {
        Node parentNode = element.getParentNode();
        if (parentNode instanceof Element) {
            Element parentElemet = (Element) parentNode;
            String prfx = XMLUtil.evaluate("prefix/text()", parentElemet);
            if (Auxilary.isEmptyOrNull(prfx)) {
                return loadPrefix(parentElemet);
            }
            return prfx;
        }
        return "";
    }

    Executer buildExecuter(String type) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String className = actionsAlias.get(type);
        if (Auxilary.isEmptyOrNull(className)) {
            throw new RuntimeException("class from type " + type + " not register");
        }
        if (!Auxilary.isEmptyOrNull(type)) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class cls1 = classLoader.loadClass(className);
            Executer ret = (Executer) cls1.newInstance();
            return ret;
        }
        throw new RuntimeException("class from type not register");
    }
}
