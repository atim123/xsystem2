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
public class ThreadContext {
    static ThreadLocal<ThreadContext> currentContext = new ThreadLocal<ThreadContext>();
    Map context=new HashMap();
    
    public static void set(Object key,Object value){
        ThreadContext ctx= currentContext.get();
        if (ctx==null){
           ctx=new ThreadContext();
           currentContext.set(ctx);
        }
        ctx.context.put(key, value);
    } 
    
    public static Object get(Object key){
        ThreadContext ctx= currentContext.get();
        if (ctx==null){
           return null; 
        }
        Object value=ctx.context.get(key);
        return value;
    }
    
    public static void clear(){
        ThreadContext ctx= currentContext.get();
        if (ctx!=null){
            ctx.context.clear();
        }
        currentContext.set(null);
    }
}
