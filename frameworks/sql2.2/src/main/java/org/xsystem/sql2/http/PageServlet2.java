/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.JXPathContext;
import org.w3c.dom.Document;
import org.xsystem.http.RestApiTemplate;
import org.xsystem.system.sql.JDBCTransationManager2;

import static org.xsystem.sql2.http.Enviroment.FILEPRFX;
//import org.xsystem.sql2.dml.SqlHelper;
import org.xsystem.sql2.http.impl.ActionExecuter;
import org.xsystem.sql2.http.impl.Config;
import org.xsystem.sql2.http.impl.FileFormat;
import org.xsystem.sql2.http.impl.HttpHelper;
import org.xsystem.sql2.http.impl.PageLoader;
import org.xsystem.utils.Auxilary;
import org.xsystem.utils.FileTransfer;
import org.xsystem.utils.XMLUtil;

/**
 *
 * @author atimofeev
 */
public class PageServlet2 extends RestApiTemplate {

    String repositoryPath;
    ServletConfig config;

    Config pagesConfig = null;
    volatile String errorReport = null;

    static ThreadLocal<Enviroment> enviromentLocal = new ThreadLocal();

    FilesWatcher fileWatcher = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.config = config;
        repositoryPath = config.getInitParameter("repository");
        repositoryPath = config.getServletContext().getRealPath(repositoryPath);
        loadRepository();
    }

    
    
    synchronized void loadRepository() {
        
        PageLoader loader = new PageLoader();
        EnviromentImpl enviroment = createEnviroment();
        pagesConfig = null;
        try {
            loader.load(repositoryPath);
            pagesConfig = new Config();
            pagesConfig.setConnectionManager(loader.getConnectionManager());
            pagesConfig.setActions(loader.getActions());
            pagesConfig.setConnections(loader.getConnections());
            pagesConfig.setAfterCreateConnectionEventListeners(loader.getAfterCreateConnectionEventListeners());
            pagesConfig.setBeforeCloseConnectionEventListeners(loader.getBeforeCloseConnectionEventListeners());
            pagesConfig.setFileStorages(loader.getFileStorages());
            pagesConfig.setErrorHandler(loader.getErrorHandler());
            pagesConfig.setObjectNames(loader.getObjectNames());
            
            Set<File> fileSet = loader.getFileSet();
            List<File> listFile = new ArrayList();
            listFile.addAll(fileSet);

            if (fileWatcher != null) {
                if (fileWatcher.isStopped()) {
                    fileWatcher.stopThread();
                }
            }
            fileWatcher = new FilesWatcher(listFile,
                    (f -> {
                        changeFile(f);
                    }));
            fileWatcher.start();

        } catch (RuntimeException ex) {
            ex.printStackTrace();
            errorReport = Auxilary.throwableToString(ex);
        } finally {
            destroyEnviroment(enviroment);
        }
    }

    synchronized void changeFile(File f) {
        fileWatcher.stopThread();
        loadRepository();
    }

    synchronized void chekLoadRepository() {
    }

    synchronized Config getConfig() {
        return pagesConfig;
    }

    static Enviroment getEnviroment() {
        return enviromentLocal.get();
    }

    EnviromentImpl createEnviroment() {
        EnviromentImpl enviroment = new EnviromentImpl();
        enviromentLocal.set(enviroment);
        return enviroment;
    }

    void destroyEnviroment(EnviromentImpl enviroment) {
        if (enviroment != null) {
            enviroment.close();
        }
        enviromentLocal.remove();
    }

    @Override
    protected Object getJson(ServletInputStream input) throws IOException {
        String json = getContentAsString(input);
        if (Auxilary.isEmptyOrNull(json)) {
            return new HashMap();
        }
        Gson gson = gsonBuilder.create();
        Object jsonContext = gson.fromJson(json, Object.class);
        return jsonContext;
    }

    static Map<String, Object> getContext(Map<String, String> evals, Map<String, Object> reqContext) {
        JXPathContext context = JXPathContext.newContext(reqContext);
        context.setFunctions(new ClassFunctions(Base64Decode.class, "BASE64"));
        context.setLenient(true);

        Map ret = new HashMap();
        evals.entrySet().forEach(entry -> {
            String key = entry.getKey();
            String eval = entry.getValue();
            Object value = context.getValue(eval);
            ret.put(key, value);
        });

        return ret;
    }

    void onCreateConnection(Config cofig, String connectionName) {//, Connection connection) {
        AfterCreateConnectionEventListener listener = cofig.getAfterCreateConnectionEventListeners().get(connectionName);
        if (listener != null) {
            Connection connection = JDBCTransationManager2.getConnection();
            listener.onCreateConnection(connection);
        }
    }

    void onCloseConnection(Config cofig, String connectionName) {//, Connection connection) {
        BeforeCloseConnectionEventListener listener = cofig.getBeforeCloseConnectionEventListeners().get(connectionName);

        if (listener != null) {
            try {
                Connection connection = JDBCTransationManager2.getConnection();
                listener.onCloseConnection(connection);
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }
    }

    Object execute(HttpServletRequest request, Config cofig, ActionExecuter action, Map params) {

        EnviromentImpl enviroment = createEnviroment();
        String connectionName = action.getConnectionName();
        enviroment.setHttpRequest(request);
        enviroment.setObjectNames(pagesConfig.getObjectNames());
        String path = action.getLocation();
        File f = enviroment.getFile(path);
        Document doc = XMLUtil.getDocumentE(f);

        enviroment.setDocument(doc);

        JDBCTransationManager2 tm = new JDBCTransationManager2(() -> cofig.getConnection(connectionName));

        Executer executer = action.getAction();
        boolean isError = true;
        try {
            tm.begin();
            onCreateConnection(cofig, connectionName);
            ActionEventListener beforeEvent = action.getBeforeEvent();
            if (beforeEvent != null) {
                beforeEvent.onEvent(params);
            }
            Object ret = executer.execute(params);
            ActionEventListener afterEvent = action.getAfterEvent();
            if (afterEvent != null) {
                if (ret instanceof Map){
                    params.putAll((Map)ret); // Залепа
                    ret=params;        
                }
                afterEvent.onEvent(ret);
            }
            onCloseConnection(cofig, connectionName);
            tm.commit();
            isError = false;
            return ret;
        } finally {
            if (isError) {
                tm.rollback();
            }
            destroyEnviroment(enviroment);
            tm.close();
        }

    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //  response.setHeader("Pragma", "No-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        //response.setHeader("Cache-Control", "no-cache");

        response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String path = request.getPathInfo();
        ErrorHandler errorHansdler = null;
        try (ServletOutputStream out = response.getOutputStream();
                ServletInputStream input = request.getInputStream()) {
            try {

                if (path == null) {
                    loadRepository();
                    if (errorReport != null) {
                        Map error = Auxilary.makeJsonError("Loading error-" + errorReport);
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        writeJson(error, out);
                    } else {
                        writeJson(Auxilary.makeJsonSuccess(), out);
                    }
                    return;
                }
                if (errorReport != null) {
                    Map error = Auxilary.makeJsonError("Loading error-" + errorReport);
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    writeJson(error, out);
                    return;
                }
                Config cofig = getConfig();
                errorHansdler = cofig.getErrorHandler();

                Matcher matcher = Pattern.compile("").matcher(path);
                List<ActionExecuter> lstActions = cofig.getActions();
                Optional<ActionExecuter> opt = lstActions.stream()
                        .filter(action -> matcher.reset().usePattern(action.getPattern()).find())
                        .findFirst();
                if (opt.isPresent()) {
                    ActionExecuter action = opt.get();

                    List groups = HttpHelper.getGroups(matcher);

                    Map params = Collections.emptyMap();
                    Object json = Collections.emptyMap();
                    Long skip = null;
                    Integer total = null;

                    if (action.isMultipart()) {
                       json=HttpHelper.getMultipartJson(request);
                    } else {
                        params = HttpHelper.getParams(request);
                        skip = HttpHelper.getParamSkip(request);
                        total = HttpHelper.getParamTotal(request);
                        if (!action.isForm()) {
                            json = getJson(input);
                        }
                    }

                    Map<String, Object> context = Auxilary.newMap(
                            "groups", groups,
                            "params", params,
                            "json", json,
                            "request", request,
                            "servletConfig",this.getServletConfig()
                    );

                    Map<String, String> evals = action.getContextParams();
                    FileFormat fileFormat = action.getFileFormat();
                    int thumb = HttpHelper.getThumb(fileFormat, context);
                    context = getContext(evals, context);
                    if (skip != null) {
                        context.put("skip", skip);
                    }
                    if (total != null) {
                        context.put("total", total);
                    }
                    Object rezult = execute(request, cofig, action, context);
                    if (fileFormat == null) {
                        writeJson(Auxilary.makeJsonSuccess("data", rezult), out);
                    } else {
                        FileTransfer fileTransfer = HttpHelper.getFileTransfer(fileFormat, rezult,
                                (format, data) -> {
                                    return getContent(format, data, "defualt");
                                }
                        );

                        if (fileTransfer == null
                                && !fileFormat.isDownload() && !Auxilary.isEmptyOrNull(fileFormat.getNotfound())) {
                            String fname = fileFormat.getNotfound();
                            if (fname.startsWith(FILEPRFX)) {
                                fname = fname.substring(FILEPRFX.length());
                                fname = config.getServletContext().getRealPath(fname);
                                File f = new File(fname);
                                if (f.exists()) {
                                    fileTransfer = new FileTransfer();
                                    String contentType = Files.probeContentType(Paths.get(fname));
                                    String fileType = Auxilary.getFileExtention(fname);
                                    byte[] b = Auxilary.readBinaryFile(f);
                                    fileTransfer.setContentType(contentType);
                                    fileTransfer.setFileType(fileType);
                                    fileTransfer.setData(b);
                                }
                            }
                        }

                        if (fileTransfer == null) {

                            Map error = Auxilary.makeJsonError("File not found ");
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            writeJson(error, out);
                        } else {
                            boolean isDownload = fileFormat.isDownload();

                            HttpHelper.writeFile(fileTransfer, isDownload, thumb, request, response, out);
                        }
                    }
                } else {
                    Map error = Auxilary.makeJsonError("Page not found [" + path + "]");
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    writeJson(error, out);
                }

            } catch (Throwable err) {
                
                // response.setStatus();
                if (errorHansdler == null) {
                    error(err, out);
                } else {
                    Object error = errorHansdler.handler(err);
                    writeJson(error, out);
                }
            }
        }
    }

    byte[] getContent(String srcType, Object data, String storage) {
        String type = srcType.trim().toLowerCase();
        if (data == null) {
            return null;
        }
        switch (type) {
            case "blob": {
                byte[] content = (byte[]) data;
                return content;
            }
            case "file": {
                String path = (String) data;
                byte[] content = getFileContent(storage, path);
                return content;
            }
        }
        return null;
    }

    byte[] getFileContent(String storageName, String path) {
        Map<String, String> fileStorages = pagesConfig.getFileStorages();
        if (fileStorages == null) {
            throw new Error("fileStorages not define");
        }

        String res = fileStorages.get(storageName);
        if (res == null) {
            return null;
        }

        String rootpath = Auxilary.getJndiResource(res, String.class);
        File f = new File(rootpath, path);
        try {
            byte[] ret = Auxilary.readBinaryFile(f);
            return ret;
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    class EnviromentImpl implements Enviroment {
        Map<String, String> objectNames;
        //Connection connection = null;
        Document document = null;
        HttpServletRequest request = null;

        @Override
        public Map<String, String> getObjectNames(){
            return this.objectNames;
        }
        
        void setObjectNames(Map<String, String> objectNames){
            this.objectNames=objectNames;
        }
        //void setConnection(Connection connection) {
        //    this.connection = connection;
        //}
        void setDocument(Document document) {
            this.document = document;
        }

        void setHttpRequest(HttpServletRequest request) {
            this.request = request;
        }

        public HttpServletRequest getHttpRequest() {
            return request;
        }

        void close() {
            request = null;
//  Auxilary.close(connection);
        }

        @Override
        public Connection getConnection() {
            return JDBCTransationManager2.getConnection();
        }

        @Override
        public Document getDocument() {
            return document;
        }

        @Override
        public File getFile(String fname) {
            if (fname.startsWith(FILEPRFX)) {
                fname = fname.substring(FILEPRFX.length());
                String path = config.getServletContext().getRealPath(fname);
                File f = new File(path);
                return f;
            }
            return null;
        }

    }

}
