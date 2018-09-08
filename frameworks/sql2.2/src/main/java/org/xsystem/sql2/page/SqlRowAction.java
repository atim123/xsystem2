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
import org.xsystem.sql2.dml.DmlCommand;
import org.xsystem.sql2.dml.DmlParams;
import org.xsystem.sql2.page.mvel.PageMVELFactory;

/**
 *
 * @author atimofeev
 */
public class SqlRowAction extends SqlAction{
    
    @Override
    public Object exec(Connection connection, Map<String, Object> params,DmlCancel dog) {
        Map<String, Object> context = new LinkedHashMap();
        context.putAll(params);
        PageMVELFactory elFactory = new PageMVELFactory(context, connection);
        buildDefinitions(elFactory, context);
        String sql=buildSql(elFactory);
        
        DmlCommand dml=new DmlCommand();
        dml.setDog(dog);
        dml.setNamedParams(this.isNamedparameters());
        dml.setUpperTag(this.getUpperTag());
        
        
        List<DmlParams> dmlParams= buildDmlParams(elFactory);
        Map<String, Object> prms =buildParams(elFactory,params);
        Object ret=dml.row(connection, sql, dmlParams, prms);
 
        return ret;
    }

    @Override
    public Stream<Map<String, Object>> execQuery(Connection connection, Long skip, Integer total, Map<String, Object> params,DmlCancel dog) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void batch(Connection connection, List<Map<String, Object>> params,DmlCancel dog) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
