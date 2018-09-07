/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xsystem.utils;
import java.util.*;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author atimofeev
 */
public class NamespaceContextImpl implements NamespaceContext{
    private Map<String, String> context = new HashMap<>();

    public NamespaceContextImpl() {
    }

    public NamespaceContextImpl(Map<String, String> context) {
        this.context = context;
    }

    public void set(String prfx, String uri) {
        context.put(prfx, uri);
    }
// setNameNamespaceURI(String prfx, String uri)
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new NullPointerException("Null prefix");
        }
        String res = context.get(prefix);
        return res;
    }

    // This method isn't necessary for XPath processing. but needed
    @Override
    public String getPrefix(String uri) {
        Iterator<String> itr=context.keySet().iterator();
        while (itr.hasNext()){
           String key=itr.next();
           String value=context.get(key);
           if (value.equals(uri))
              return key; 
        }
        return null;
    }

    // This method isn't necessary for XPath processing either.
    @Override
    public Iterator getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    }
}
