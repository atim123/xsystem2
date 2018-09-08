/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.convertor;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author atimofeev
 */
public class DefaultConvertor implements Converter {

    protected String coerceToString(Object value) {

        if (value instanceof String) {
            return (String) value;
        }
        if (value instanceof Enum<?>) {
            return ((Enum<?>) value).name();
        }
//        if (value instanceof Double) {
//            Double d = (Double) value; 
//            String s = NumberFormat.getNumberInstance(Locale.US).format(d);
//            return s;
//        }
//        if (value instanceof Float) {
//            Float d = (Float) value;
//            String s = NumberFormat.getNumberInstance(Locale.US).format(d);
//            return s;
//        }
        if (value instanceof Double || value instanceof Float) {
            BigDecimal bd = BigDecimal.valueOf((Double) value);
            String bds = bd.toPlainString();
            String ss[] = bds.split("\\.");
            if (ss.length == 1 || Integer.parseInt(ss[1]) == 0) {
                return ss[0];
            } else {
                return bds;
            }
        }
        if (value instanceof java.util.Date) {
            if (value instanceof java.sql.Time) {
                java.sql.Time t = (java.sql.Time) value;
                String s = t.toString();
                return s;
            }

            java.util.Date d = (java.util.Date) value;
            long l = d.getTime();
            String s = ISODateTimeFormat.dateTimeNoMillis().print(l);
            return s;
        }

        return value.toString();
    }

    protected Long coerceToLong(Object value) {

        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return Long.valueOf(((Number) value).longValue());
        }
        if (value instanceof String) {
            try {
                return Long.valueOf((String) value);
            } catch (NumberFormatException e) {
                throw new ConvertorException("Can't convert " + value + " to " + Long.class);
            }
        }
        if (value instanceof Character) {
            return Long.valueOf((short) ((Character) value).charValue());
        }
        throw new ConvertorException("Can't convert " + value + " to " + Long.class);
    }

    protected Double coerceToDouble(Object value) {

        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            try {
                String str = (String) value;
                if (str.isEmpty()){
                    return null;
                }
                double d = NumberFormat.getNumberInstance(Locale.US).parse(str).doubleValue();
                return d;
            } catch (NumberFormatException | ParseException e) {
                throw new ConvertorException("Can't convert " + value + " to " + Double.class);
            }
        }
        if (value instanceof Character) {
            return Double.valueOf((short) ((Character) value).charValue());
        }
        throw new ConvertorException("Can't convert " + value + " to " + Double.class);

    }

    protected BigDecimal coerceToBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }
        if (value instanceof Number) {
            return new BigDecimal(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            try {
                return new BigDecimal((String) value);
            } catch (NumberFormatException e) {
                throw new ConvertorException("Can't convert " + value + " to " + BigDecimal.class);
            }
        }
        if (value instanceof Character) {
            return new BigDecimal((short) ((Character) value).charValue());
        }
        throw new ConvertorException("Can't convert " + value + " to " + BigDecimal.class);
    }

    protected BigInteger coerceToBigInteger(Object value) {
        if (value == null || "".equals(value)) {
            return BigInteger.valueOf(0l);
        }
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toBigInteger();
        }
        if (value instanceof Number) {
            return BigInteger.valueOf(((Number) value).longValue());
        }
        if (value instanceof String) {
            try {
                return new BigInteger((String) value);
            } catch (NumberFormatException e) {
                throw new ConvertorException("Can't convert " + value + " to " + BigInteger.class);
            }
        }
        if (value instanceof Character) {
            return BigInteger.valueOf((short) ((Character) value).charValue());
        }
        throw new ConvertorException("Can't convert " + value + " to " + BigInteger.class);
    }

    protected Float coerceToFloat(Object value) {

        if (value instanceof Float) {
            return (Float) value;
        }
        if (value instanceof Number) {
            return Float.valueOf(((Number) value).floatValue());
        }
        if (value instanceof String) {
            try {
                String str = (String) value;
                Float f = NumberFormat.getNumberInstance(Locale.US).parse(str).floatValue();
                return f;
            } catch (NumberFormatException | ParseException e) {
                throw new ConvertorException("Can't convert " + value + " to " + Float.class);
            }
        }
        if (value instanceof Character) {
            return Float.valueOf((short) ((Character) value).charValue());
        }
        throw new ConvertorException("Can't convert " + value + " to " + Float.class);
    }

    protected Boolean coerceToBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.valueOf((String) value);
        }
        
        if (value instanceof Number){
            Number num=(Number) value;
            int i=num.intValue();
            return i>0;
        }
        throw new ConvertorException("Can't convert " + value + " to " + Boolean.class);
    }

    protected Integer coerceToInteger(Object value) {

        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
        }
        if (value instanceof String) {
            try {
                return Integer.valueOf((String) value);
            } catch (NumberFormatException e) {
                throw new ConvertorException("Can't convert " + value + " to " + Integer.class);
            }
        }
        if (value instanceof Character) {
            return Integer.valueOf((short) ((Character) value).charValue());
        }
        throw new ConvertorException("Can't convert " + value + " to " + Integer.class);
    }

    protected Short coerceToShort(Object value) {
        if (value == null || "".equals(value)) {
            return Short.valueOf((short) 0);
        }
        if (value instanceof Short) {
            return (Short) value;
        }
        if (value instanceof Number) {
            return Short.valueOf(((Number) value).shortValue());
        }
        if (value instanceof String) {
            try {
                return Short.valueOf((String) value);
            } catch (NumberFormatException e) {
                throw new ConvertorException("Can't convert " + value + " to " + Short.class);
            }
        }
        if (value instanceof Character) {
            return Short.valueOf((short) ((Character) value).charValue());
        }
        throw new ConvertorException("Can't convert " + value + " to " + Short.class);
    }

    protected Byte coerceToByte(Object value) {

        if (value instanceof Byte) {
            return (Byte) value;
        }
        if (value instanceof Number) {
            return Byte.valueOf(((Number) value).byteValue());
        }
        if (value instanceof String) {
            try {
                return Byte.valueOf((String) value);
            } catch (NumberFormatException e) {
                throw new ConvertorException("Can't convert " + value + " to " + Byte.class);
            }
        }
        if (value instanceof Character) {
            return Byte.valueOf(Short.valueOf((short) ((Character) value).charValue()).byteValue());
        }
        throw new ConvertorException("Can't convert " + value + " to " + Byte.class);
    }

    protected Character coerceToCharacter(Object value) {

        if (value instanceof Character) {
            return (Character) value;
        }
        if (value instanceof Number) {
            return Character.valueOf((char) ((Number) value).shortValue());
        }
        if (value instanceof String) {
            return Character.valueOf(((String) value).charAt(0));
        }
        throw new ConvertorException("Can't convert " + value + " to " + Character.class);
    }

    @SuppressWarnings("unchecked")
    protected <T extends Enum<T>> T coerceToEnum(Object value, Class<T> type) {
        if (value == null || "".equals(value)) {
            return null;
        }
        if (type.isInstance(value)) {
            return (T) value;
        }
        if (value instanceof String) {
            try {
                return Enum.valueOf(type, (String) value);
            } catch (IllegalArgumentException e) {
                throw new ConvertorException("Can't convert " + value + " to " + type);
            }
        }
        throw new ConvertorException("Can't convert " + value + " to " + type);
    }

    protected java.sql.Date coerceToSqlDate(Object value) {
        if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        }

        if (value instanceof Long) {
            Long l = (Long) value;
            java.sql.Date d = new java.sql.Date(l);
            return d;
        }

        if (value instanceof java.util.Date) {
            java.util.Date jd = (java.util.Date) value;
            Long l = jd.getTime();
            java.sql.Date d = new java.sql.Date(l);
            return d;
        }

        if (value instanceof String) {
            String s = (String) value;
            try {
                long l = ISODateTimeFormat.dateTimeNoMillis().parseMillis(s);

                java.sql.Date d = new java.sql.Date(l);
                return d;
            } catch (UnsupportedOperationException | IllegalArgumentException ex) {
                throw new ConvertorException("Can't convert " + value + " to " + java.sql.Date.class);
            }
        }

        throw new ConvertorException("Can't convert " + value + " to " + java.sql.Date.class);
    }

    protected java.sql.Timestamp coerceToSqlTimestamp(Object value) {
        if (value instanceof java.sql.Timestamp) {
            return (java.sql.Timestamp) value;
        }
        if (value instanceof java.util.Date) {
            java.util.Date jd = (java.util.Date) value;
            Long l = jd.getTime();
            java.sql.Timestamp d = new java.sql.Timestamp(l);
            return d;
        }

        if (value instanceof String) {
            String s = (String) value;
            try {
                long l = ISODateTimeFormat.dateTimeNoMillis().parseMillis(s);

                java.sql.Timestamp d = new java.sql.Timestamp(l);
                return d;
            } catch (UnsupportedOperationException | IllegalArgumentException ex) {
                throw new ConvertorException("Can't convert " + value + " to " + java.sql.Timestamp.class);
            }
        }

        throw new ConvertorException("Can't convert " + value + " to " + java.sql.Timestamp.class);
    }

    protected java.sql.Time coerceToSqlTime(Object value) {
        if (value instanceof java.sql.Time) {
            return (java.sql.Time) value;
        }
        if (value instanceof java.util.Date) {
            java.util.Date jd = (java.util.Date) value;
            Long l = jd.getTime();
            java.sql.Time d = new java.sql.Time(l);
            return d;
        }
        if (value instanceof String) {
            String s = (String) value;
            try {
                java.sql.Time t = java.sql.Time.valueOf(s);
                return t;
            } catch (UnsupportedOperationException | IllegalArgumentException ex) {
                throw new ConvertorException("Can't convert " + value + " to " + java.sql.Time.class);
            }
        }
        throw new ConvertorException("Can't convert " + value + " to " + java.sql.Time.class);
    }

    java.util.Date coerceToDate(Object value) {
        if (value instanceof java.util.Date) {
            return (java.util.Date) value;
        }
        if (value instanceof String) {
            String s = (String) value;
            try {
                long l = ISODateTimeFormat.dateTimeNoMillis().parseMillis(s);

                java.util.Date d = new java.util.Date(l);
                return d;
            } catch (UnsupportedOperationException | IllegalArgumentException ex) {
                throw new ConvertorException("Can't convert " + value + " to " + java.util.Date.class);
            }
        }
        throw new ConvertorException("Can't convert " + value + " to " + java.util.Date.class);
    }

    @SuppressWarnings("unchecked")
    protected Object coerceToType(Object value, Class<?> type) {

        if (value == null) {
            return null;
        }
        if (type == String.class) {
            return coerceToString(value);
        }

        if (type == Long.class || type == long.class) {
            return coerceToLong(value);
        }

                
        if (type == Double.class || type == double.class) {
            return coerceToDouble(value);
        }

        if (type == Boolean.class || type == boolean.class) {
            return coerceToBoolean(value);
        }

        if (type == Integer.class || type == int.class) {
            return coerceToInteger(value);
        }

        if (type == Float.class || type == float.class) {
            return coerceToFloat(value);
        }

        if (type == Short.class || type == short.class) {
            return coerceToShort(value);
        }

        if (type == Byte.class || type == byte.class) {
            return coerceToByte(value);
        }

        if (type == Character.class || type == char.class) {
            return coerceToCharacter(value);
        }

        if (type == BigDecimal.class) {
            return coerceToBigDecimal(value);
        }

        if (type == BigInteger.class) {
            return coerceToBigInteger(value);
        }

        if (type.getSuperclass() == Enum.class) {
            return coerceToEnum(value, (Class<? extends Enum>) type);
        }

        if (value.getClass() == type || type.isInstance(value)) {
            return value;
        }

        if (type == java.sql.Date.class) {
            return coerceToSqlDate(value);
        }

        if (type == java.sql.Timestamp.class) {
            return coerceToSqlTimestamp(value);
        }

        if (type == java.sql.Time.class) {
            return coerceToSqlTime(value);
        }

        if (type == java.util.Date.class) {
            return coerceToDate(value);
        }

        throw new ConvertorException("Can't convert " + value + " to " + type);

    }

    public <T> T convert(Object value, Class<T> type) {
        return (T) coerceToType(value, type);
    }
}


/*

 DATE java.sql.Date 
 TIME java.sql.Time 
 TIMESTAMP java.sql.Timestamp


 @SuppressWarnings("unchecked")
 protected Object coerceToType(Object value, Class<?> type) {
 if (type == String.class) {
 return coerceToString(value);
 }
 if (type == Long.class || type == long.class) {
 return coerceToLong(value);
 }
 if (type == Double.class || type == double.class) {
 return coerceToDouble(value);
 }
 if (type == Boolean.class || type == boolean.class) {
 return coerceToBoolean(value);
 }
 if (type == Integer.class || type == int.class) {
 return coerceToInteger(value);
 }
 if (type == Float.class || type == float.class) {
 return coerceToFloat(value);
 }
 if (type == Short.class || type == short.class) {
 return coerceToShort(value);
 }
 if (type == Byte.class || type == byte.class) {
 return coerceToByte(value);
 }
 if (type == Character.class || type == char.class) {
 return coerceToCharacter(value);
 }
 if (type == BigDecimal.class) {
 return coerceToBigDecimal(value);
 }
 if (type == BigInteger.class) {
 return coerceToBigInteger(value);
 }
 if (type.getSuperclass() == Enum.class) {
 return coerceToEnum(value, (Class<? extends Enum>)type);
 }
 if (value == null || value.getClass() == type || type.isInstance(value)) {
 return value;
 }
 if (value instanceof String) {
 return coerceStringToType((String)value, type);
 }
 throw new ELException(LocalMessages.get("error.coerce.type", value, value.getClass(), type));
 }


 CHAR String 
 VARCHAR String  
 LONGVARCHAR String 
 NUMERIC java.math.BigDecimal 
 DECIMAL java.math.BigDecimal 
 BIT boolean 
 BOOLEAN boolean 
 TINYINT byte 
 SMALLINT short 
 INTEGER int 
 BIGINT long 
 REAL float 
 FLOAT double 
 DOUBLE double 
 BINARY byte[] 
 VARBINARY byte[] 
 LONGVARBINARY byte[] 
 DATE java.sql.Date 
 TIME java.sql.Time 
 TIMESTAMP java.sql.Timestamp 
 CLOB Clob 
 BLOB Blob 
 ARRAY Array 
 DISTINCT mapping of underlying type 
 STRUCT Struct 
 REF Ref 
 DATALINK java.net.URL 
 JAVA_OBJECT underlying Java class 

 */
