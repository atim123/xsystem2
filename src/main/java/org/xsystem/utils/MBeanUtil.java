/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 *
 * @author tim
 */
public class MBeanUtil {
    
    public static ObjectName createObjectName(String strName){
        try {  
            ObjectName ret=new ObjectName(strName);
            return ret;
        } catch (MalformedObjectNameException ex) {
            throw new Error(ex);
        }
    }
    
    public static Object invoke(MBeanServer mbs,ObjectName name, String operationName,
                         Object params[], String signature[]){
        try {
            Object ret=mbs.invoke(name, operationName, params, signature);
            return ret;
        } catch (InstanceNotFoundException | MBeanException | ReflectionException ex) {
            throw new Error(ex);
        }
        
    }
    
    public static String  unregisterMBean(MBeanServer mbs,ObjectName name){
        if (name==null){
            return null;
        }
        try {
            mbs.unregisterMBean(name);
            return null;
        } catch (InstanceNotFoundException | MBeanRegistrationException ex) {
            return ex.getMessage();
        }
    }
    
    public static ObjectInstance registerMBean(MBeanServer mbs,Object object, ObjectName name,boolean unreg){
        if (unreg){
            if (mbs.isRegistered(name)){
                unregisterMBean(mbs,name);
            }
        }
        try {
            ObjectInstance ret=mbs.registerMBean(object, name);
            return ret;
        } catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException ex) {
           throw new Error(ex);         
        }
    }
    
    
}


