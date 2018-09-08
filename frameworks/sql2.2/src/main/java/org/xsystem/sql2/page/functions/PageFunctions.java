/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.page.functions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.jxpath.JXPathContext;
import org.xsystem.utils.DateUtil;

/**
 *
 * @author atimofeev
 */
public class PageFunctions {
   Map actionContext;
    public PageFunctions(Map actionContext){
          this.actionContext=actionContext;
    } 
    
    public List list(Object... values) {
            List lst = new ArrayList();
            int size = values.length;
            for (int i = 0; i < size; i++) {
                Object cur = values[i];
                lst.add(cur);
            }
            return lst;
        }

        public Set set(Object... values) {
            Set lst = new HashSet();
            int size = values.length;
            for (int i = 0; i < size; i++) {
                Object cur = values[i];
                lst.add(cur);
            }
            return lst;
        }

        public Map structure(Object... args) {
            LinkedHashMap res = new LinkedHashMap();
            if (args == null) {
                return res;
            }
            int size = args.length / 2;
            for (int i = 0; i < size; i++) {
                int idx = 2 * i;
                String key = args[idx].toString();
                Object value = args[idx + 1];
                res.put(key, value);
            }
            return res;
        }

        public Boolean in(String path, Object... values) {
            JXPathContext context = JXPathContext.newContext(actionContext);
            Object testvalue = context.getValue(path);
            if (testvalue == null) {
                return false;
            }
            int size = values.length;
            for (int i = 0; i < size; i++) {
                Object cur = values[i];
                if (cur instanceof List) {
                    List lst = (List) cur;
                    Set set = new HashSet(lst);
                    if (set.contains(testvalue)) {
                        return true;
                    }
                } else if (cur instanceof Set) {
                    Set set = (Set) cur;
                    if (set.contains(testvalue)) {
                        return true;
                    }
                } else if (testvalue.equals(cur)) {
                    return true;
                }
            }
            return false;
        }

        public Object path(String path) {
            JXPathContext context = JXPathContext.newContext(actionContext);
            context.setLenient(true);
            Object ret = context.getValue(path);
            return ret;
        }

        public Boolean exist(String path) {
            JXPathContext context = JXPathContext.newContext(actionContext);
            context.setLenient(true);
            boolean ret = context.getValue(path) != null;
            return ret;
        }

        public String guid() {
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();
            return randomUUIDString;
        }

        public boolean isGuid(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof String) {
                String uuid = (String) obj;
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
            return false;
        }

        public String strRepeat(final String str, final String separator, final int repeat) {
            boolean flag = false;
            String ret = "";
            for (int i = 1; i <= repeat; i++) {
                if (flag) {
                    ret = ret + separator;
                }
                ret = ret + str;
                flag = true;
            }
            return ret;
        }

        public boolean isBytes(Object o) {
            boolean ret = o instanceof byte[];
            return ret;
        }

        public boolean isMap(Object o) {
            boolean ret = o instanceof Map;
            return ret;
        }

        public boolean isList(Object o) {
            boolean ret = o instanceof List;
            return ret;
        }

        public boolean isString(Object o) {
            boolean ret = o instanceof String;
            return ret;
        }

        public boolean isNumber(Object o) {
            boolean ret = (o instanceof Long) || (o instanceof Integer) || (o instanceof Double);
            return ret;
        }

        public boolean isLong(Object o) {
            boolean ret = (o instanceof Long);
            return ret;
        }

        public boolean isInt(Object o) {
            boolean ret = (o instanceof Integer);
            return ret;
        }

        public boolean isDouble(Object o) {
            boolean ret = (o instanceof Double);
            return ret;
        }

        public boolean isDate(Object o) {
            boolean ret = (o instanceof Date);
            return ret;
        }

        public String nowISODateAsString(){
            return DateUtil.nowISODateAsString();
        }
        
}
