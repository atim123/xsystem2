/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.page;

import java.sql.Connection;
import java.util.ArrayList;
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
 * @author Andrey Timofeev
 */
public class SqlBatchAction extends SqlAction {

    

    @Override
    public void batch(Connection connection, List<Map<String, Object>> params,DmlCancel dog) {
       String sql =  getElSql();
       DmlCommand dml=new DmlCommand();
       dml.setDog(dog);
       
       List<DmlParams> dmlParams= buildBatchParams();
       List<Map<String, Object>> resParam=new ArrayList();
       
       for (Map<String, Object> row:params){
           Map<String, Object> context = new LinkedHashMap();
           PageMVELFactory elFactory = new PageMVELFactory(context, connection);
           Map<String, Object> prms =buildParams(elFactory,row);
           resParam.add(prms);
       }
       dml.batch(connection, sql, dmlParams, resParam);
    }

    @Override
    public Object exec(Connection connection, Map<String, Object> params,DmlCancel dog) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Stream<Map<String, Object>> execQuery(Connection connection, Long skip, Integer total, Map<String, Object> params,DmlCancel dog) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
