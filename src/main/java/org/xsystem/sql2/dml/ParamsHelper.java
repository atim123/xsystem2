/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.dml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author atimofeev
 */
public interface ParamsHelper {

    
    public static Map<String, Object> params(Object... args) {
        return Auxilary.newMap(args);
    }
    
    public static List list(Object... args) {
       List res = new ArrayList();
        if (args != null) {
            res.addAll(Arrays.asList(args));
        }
        return res;
    }
}
