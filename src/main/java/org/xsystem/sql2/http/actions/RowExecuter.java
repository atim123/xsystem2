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
public class RowExecuter extends DMLExecuter {

    @Override
    public Object execute(Map params) throws RuntimeException {
        Object ret = Executer.row(path, params);
        return ret;
    }
}
