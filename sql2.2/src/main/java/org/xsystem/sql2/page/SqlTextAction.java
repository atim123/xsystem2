/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.page;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.xsystem.sql2.dml.DmlCancel;
import org.xsystem.sql2.page.mvel.PageMVELFactory;

/**
 *
 * @author Andrey Timofeev
 */
public class SqlTextAction extends SqlAction{

    @Override
    public Object exec(Connection connection, Map<String, Object> params,DmlCancel dog) {
        Map<String, Object> context = new LinkedHashMap();
        context.putAll(params);
        PageMVELFactory elFactory = new PageMVELFactory(context, connection);
        buildDefinitions(elFactory, context);
        String sql=buildSql(elFactory);
        return sql;
    }

    @Override
    public void batch(Connection connection, List<Map<String, Object>> params,DmlCancel dog) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Stream<Map<String, Object>> execQuery(Connection connection, Long skip, Integer total, Map<String, Object> params,DmlCancel dog) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
