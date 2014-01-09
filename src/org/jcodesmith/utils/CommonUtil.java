package org.jcodesmith.utils;

import java.sql.Types;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jcodesmith.db.dal.DataBaseType;
import org.jcodesmith.db.meta.ColumnMeta;
import org.jcodesmith.db.meta.TableMeta;

public class CommonUtil {
    /**
     * 关键字
     */
    public final static String[] javaKeywords = new String[] { "private", "protected", "public", "abstract", "class",
            "extends", "final", "implements", "interface", "native", "new", "static", "strictfp", "synchronized",
            "transient", "volatile", "break", "continue", "return", "do", "while", "if", "else", "for", "instanceof",
            "switch", "case", "default", "catch", "finally", "throw", "throws", "try", "import", "package", "boolean",
            "byte", "char", "double", "float", "int", "long", "short", "null", "true", "false", "super", "this", "void" };

    public final static Class<?>[] baseTypes = new Class<?>[] { Boolean.class, Character.class, Byte.class,
            Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class, String.class };

    /**
     * This method return the Class of a specific SQL Type, defined in
     * java.sql.Types.
     * 
     * @param sqlType SQL Type.
     * @return The Class relative to the SQL Type.
     */
    public static Class<?> getClass(int sqlType) {
        switch (sqlType) {
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
            return String.class;

        case Types.BIT:
        case Types.BOOLEAN:
            return boolean.class;

        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.INTEGER:
            return Integer.class;

        case Types.BIGINT:
            return Long.class;

        case Types.FLOAT:
        case Types.DOUBLE:
        case Types.DECIMAL:
        case Types.NUMERIC:
        case Types.REAL:
            return Double.class;

        case Types.DATE:
            return java.util.Date.class;

        case Types.TIMESTAMP:
        case Types.TIME:
            return java.util.Date.class;
        default:
            return String.class;
            /*
             * types not recognized yet case Types.ARRAY : case Types.BINARY :
             * case Types.BLOB : case Types.CLOB : case Types.DATALINK : case
             * Types.DISTINCT : case Types.JAVA_OBJECT : case
             * Types.LONGVARBINARY : case Types.NULL : case Types.OTHER : case
             * Types.REF : case Types.STRUCT : case Types.VARBINARY :
             */
        }
    }

    public static String propertyType(ColumnMeta column) {
        if (column == null) {
            return "";
        }
        return getClass(column.getSqlType()).getSimpleName();
    }

    /**
     * 类头导入
     * 
     * @param table
     * @return
     */
    public static String classImportStr(TableMeta table) {
        List<String> list = new ArrayList<String>();
        for (ColumnMeta c : table.getColumns()) {
            Class<?> cls = getClass(c.getSqlType());
            if (isBaseType(cls)) {
                continue;
            }
            String t = "import " + cls.getName() + ";\r\n";
            if (!list.contains(t)) {
                list.add(t);
            }
        }
        String ret = "";
        for (String s : list) {
            ret += s;
        }
        return ret;
    }

    public static boolean isBaseType(Class<?> c) {
        for (Class<?> b : baseTypes) {
            if (b.equals(c)) {
                return true;
            }
        }
        return c.isPrimitive();
    }

    /**
     * 类名字符串
     * 
     * @param columnType
     * @return
     */
    public static String getClassStr(int columnType) {
        return getClass(columnType).getSimpleName();
    }

    /**
     * 是否 关键字
     * 
     * @param value
     * @return
     */
    public static boolean IsJavaKeyword(String value) {
        if (value.length() > 10)
            return false;
        for (int i = 0; i < javaKeywords.length; i++) {
            if (value.equals(javaKeywords[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 驼峰命名
     * 
     * @param name
     * @param firstLower
     * @return
     */
    public static String Camel(String name, boolean firstLower) {
        try {
            String ret = name.toLowerCase();
            String[] arr = ret.split("_");
            ret = "";
            for (int i = 0; i < arr.length; i++) {
                if (i == 0 && firstLower) {
                    ret += arr[i];
                } else {
                    ret += arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1, arr[i].length());
                }
            }
            return ret;
        } catch (Exception e) {
            return name;
        }
    }

    /**
     * 
     * @param o
     * @return
     */
    public String deleteRN(String o) {
        if (o == null) {
            return "";
        }
        return o.replaceAll("\r\n", " ");
    }

    /**
     * 
     * @param o
     * @return
     */
    public String rn2br(String o) {
        return o.replaceAll("\r\n", "<br/>");
    }

    /**
     * 类名
     * 
     * @param tableName
     * @return
     */
    public static String className(TableMeta table) {
        return Camel(table.getName(), false);
    }

    public static String getColumsString(TableMeta table) {
        String sColumnList = "";
        for (ColumnMeta col : table.getColumns()) {
            if (sColumnList != "") {
                sColumnList += ",\r\n\t\t\t";
            } else {
                sColumnList += "\t\t\t";
            }
            sColumnList += col.getName();
        }
        return sColumnList;
    }

    public static String getInsertString(TableMeta table) {
        String sColumnList = "";
        String sMemberList = "";
        for (ColumnMeta col : table.getColumns()) {

            if (sColumnList != "") {
                sColumnList += ",\r\n\t\t\t";
                sMemberList += ",\r\n\t\t\t";
            }
            sColumnList += col.getName();
            if (col.getName().toUpperCase() != "GMT_CREATED" && col.getName().toUpperCase() != "GMT_MODIFIED") {
                sMemberList += MessageFormat.format("#{0}#", propertyName(col));
            } else {
                sMemberList += getSqlNowStr(table);
            }
        }
        return MessageFormat.format("insert into {0} ( \r\n\t\t\t{1} \r\n\t\t) values ( \r\n\t\t\t{2}  \r\n\t\t)",
                table.getName(), sColumnList, sMemberList);
    }

    
    public static String getSqlNowStr(TableMeta table){
        if(table.getDatabase().getDatabaseType().equals(DataBaseType.MYSQL)){
            return "now()";
        }else{
            return "sysdate";
        }
    }
    
    public static String getDBType(TableMeta table){
        if(table.getDatabase().getDatabaseType().equals(DataBaseType.MYSQL)){
            return "mysql";
        }else{
            return "oracle";
        }
    }
    /**
     * 类名
     * 
     * @param tableName
     * @return
     */
    public static String nameSpace(TableMeta table) {
        return Camel(table.getName(), true);
    }

    /**
     * 类名
     * 
     * @param tableName
     * @return
     */
    public static String className(String tableName) {
        return Camel(tableName, false);
    }

    /**
     * 属性名
     * 
     * @param columnName
     * @return
     */
    public static String propertyName(String columnName) {
        return Camel(columnName, true);
    }

    /**
     * 属性名
     * 
     * @param columnName
     * @return
     */
    public static String propertyName(ColumnMeta column) {
        return Camel(column.getName(), true);
    }

    /**
     * 属性名
     * 
     * @param columnName
     * @return
     */
    public String propertyMethodName(ColumnMeta column) {
        return Camel(column.getName(), false);
    }

    /**
     * 当前日期
     * 
     * @return
     */
    public static String now() {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(new Date());
    }

}
