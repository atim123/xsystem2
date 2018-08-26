/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http.actions;

import java.lang.management.ManagementFactory;
import java.util.Map;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.w3c.dom.Element;
import org.xsystem.sql2.convertor.Converter;
import org.xsystem.sql2.convertor.DefaultConvertor;
import org.xsystem.sql2.http.Enviroment;
import org.xsystem.sql2.http.Executer;
import org.xsystem.utils.MBeanUtil;

/**
 *
 * @author tim
 */
public class MBeanInfoExecuter implements Executer {

    private static final Converter converter = new DefaultConvertor();
    
    @Override
    public Object execute(Map params) throws RuntimeException {
        String name = (String) params.get("name");
        Enviroment env = Enviroment.getEnviroment();
        Map<String, String> objectNames = env.getObjectNames();
        String realName = objectNames.get(name);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName objname = MBeanUtil.createObjectName(realName);
        try {
            MBeanInfo info = mbs.getMBeanInfo(objname);
            return info;
        } catch (InstanceNotFoundException | IntrospectionException | ReflectionException ex) {
            throw new Error(ex);

        }
    }

    @Override
    public void parse(Element element) {
    }

}
