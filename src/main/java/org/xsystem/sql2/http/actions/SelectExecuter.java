/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http.actions;

import java.util.Map;
import org.xsystem.sql2.http.Executer;

/**
 *
 * @author atimofeev
 */
public class SelectExecuter extends DMLExecuter {

    @Override
    public Object execute(Map params) throws RuntimeException {
        Long skip = null;
        Integer total = null;
        Object obj = params.get("skip");
        if (obj instanceof String) {
            String strSkip = (String) obj;
            if (!strSkip.isEmpty()) {
                skip = Long.getLong(strSkip);
            }
        } else if (obj instanceof Long) {
              skip = (Long) obj;
        }

        obj = params.get("total");
        if (obj instanceof String) {
            String strTotal = (String) obj;
            if (!strTotal.isEmpty()) {
                total = Integer.getInteger(strTotal);
            }
        } else if (obj instanceof Integer) {
            total=(Integer) obj ;
        }

        Object ret = Executer.list(path, skip, total, params);
        return ret;
    }
}
