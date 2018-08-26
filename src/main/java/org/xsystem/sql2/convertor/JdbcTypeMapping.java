/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.convertor;

import java.sql.Types;
import org.xsystem.sql2.dml.DataAccessException;
import org.xsystem.utils.Auxilary;

/**
 *
 * @author atimofeev
 */
public class JdbcTypeMapping {
   public static int json=-999;
    
   public static Class translateToJava(int jdbcType){
        switch (jdbcType) {
            case Types.VARCHAR:
                return String.class;
            case Types.BIT:
                return Boolean.class;
            case Types.TINYINT:
                 return Byte.class;
            case Types.SMALLINT:
                return Short.class;
            case Types.INTEGER:    
                return Integer.class; 
            case Types.BIGINT:
                return Long.class;
            case Types.FLOAT:
                 return Double.class;
            case Types.REAL:
                return Float.class;
            case Types.DOUBLE:
                return Double.class;
            case Types.NUMERIC:
                return java.math.BigDecimal.class;
            case Types.DECIMAL:
                return java.math.BigDecimal.class;
            case Types.CHAR:
                return String.class;
            case Types.LONGVARCHAR:
                return String.class;
            case Types.DATE:
                return java.sql.Date.class;
            case Types.TIME:
                return java.sql.Time.class;
            case Types.TIMESTAMP:
                return java.sql.Timestamp.class;
            case Types.BINARY:
                return byte[].class;
            case Types.VARBINARY:
                return byte[].class;
            case Types.LONGVARBINARY:
                return byte[].class;
            case Types.OTHER:
                return Object.class;
            case Types.JAVA_OBJECT:
                throw new DataAccessException("Types.JAVA_OBJECT - not implemented ");
            case Types.STRUCT:
                return java.sql.Struct.class;
            case Types.ARRAY:
                return java.sql.Array.class;
            case Types.BLOB:
                return java.sql.Blob.class;
            case Types.CLOB:
                return java.sql.Clob.class;
             case Types.REF:
                 return java.sql.Ref.class;
               case Types.BOOLEAN:
                return Boolean.class;
            case Types.ROWID:
                 return java.sql.RowId.class;
            case Types.NCHAR:
                return String.class;
            case Types.NVARCHAR:
                return String.class;
            case Types.LONGNVARCHAR:
                return String.class;
            case Types.NCLOB:
                return java.sql.NClob.class;
             case Types.SQLXML:
                 return java.sql.SQLXML.class;
            case Types.REF_CURSOR:
                 throw new DataAccessException("Types.REF_CURSOR - not implemented ");
            case Types.TIME_WITH_TIMEZONE:
                throw new DataAccessException("Types.TIME_WITH_TIMEZONE - not implemented ");
            case Types.TIMESTAMP_WITH_TIMEZONE:
                throw new DataAccessException("Types.TIMESTAMP_WITH_TIMEZONE - not implemented ");
            default:
                throw new DataAccessException("invalid jdbc type -" + jdbcType);
        }
    }
   
   public static int JDBCTypeTranslate(String jdbcTypeName) {
        if (Auxilary.isEmptyOrNull(jdbcTypeName)) {
            return Types.OTHER;
        }
        switch (jdbcTypeName.toUpperCase()) {
            case "STRING":
                return Types.VARCHAR;
            case "BIT":
                return Types.BIT;
            case "TINYINT":
                return Types.TINYINT;
            case "SMALLINT":
                return Types.TINYINT;
            case "INTEGER":
                return Types.INTEGER;
            case "BIGINT":
                return Types.BIGINT;
            case "FLOAT":
                return Types.FLOAT;
            case "REAL":
                return Types.REAL;
            case "DOUBLE":
                return Types.DOUBLE;
            case "NUMERIC":
                return Types.NUMERIC;
            case "DECIMAL":
                return Types.DECIMAL;
            case "CHAR":
                return Types.CHAR;
            case "VARCHAR":
                return Types.VARCHAR;
            case "LONGVARCHAR":
                return Types.LONGVARCHAR;
            case "DATE":
                return Types.DATE;
            case "TIME":
                return Types.TIME;
            case "TIMESTAMP":
                return Types.TIMESTAMP;
            case "BINARY":
                return Types.BINARY;
            case "VARBINARY":
                return Types.VARBINARY;
            case "LONGVARBINARY":
                return Types.LONGVARBINARY;
            case "OTHER":
                return Types.OTHER;
            case "JAVA_OBJECT":
                return Types.JAVA_OBJECT;
            case "JSON":
                return json;
            case "STRUCT":
                return Types.STRUCT;
            case "ARRAY":
                return Types.ARRAY;
            case "BLOB":
                return Types.BLOB;
            case "CLOB":
                return Types.CLOB;
            case "REF":
                return Types.REF;
            case "BOOLEAN":
                return Types.BOOLEAN;
            case "ROWID":
                return Types.ROWID;
            case "NCHAR":
                return Types.NCHAR;
            case "NVARCHAR":
                return Types.NVARCHAR;
            case "LONGNVARCHAR":
                return Types.LONGNVARCHAR;
            case "NCLOB":
                return Types.NCLOB;
            case "SQLXML":
                return Types.SQLXML;
            case "REF_CURSOR":
                return Types.REF_CURSOR;
            case "TIME_WITH_TIMEZONE":
                return Types.TIME_WITH_TIMEZONE;
            case "TIMESTAMP_WITH_TIMEZONE":
                return Types.TIMESTAMP_WITH_TIMEZONE;
            default:
                throw new DataAccessException("invalid jdbc type -" + jdbcTypeName.toUpperCase());

        }
    }
}
