/**
 * 
 */
package org.jcodesmith.db.dal;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jcodesmith.db.config.DatabaseConfig;
import org.jcodesmith.db.config.DatabaseConfigManager;
import org.jcodesmith.db.meta.ColumnMeta;
import org.jcodesmith.db.meta.DatabaseMeta;
import org.jcodesmith.db.meta.TableMeta;
import org.jcodesmith.plugin.helper.PluginHelper;
import org.jcodesmith.utils.CommonUtil;

/**
 * database meta数据管理
 * 
 * @author greki.shen
 * 
 */
public class MetaManager {

    private static final String SpecialCharString = "@#$%*+";



    /**
     * 数据库下所有的数据表
     * 
     * @return
     */
    public static List<TableMeta> getTables(Connection conn,boolean isInitColumn) {
        List<TableMeta> listTbls = getTables(conn, new String[] { "TABLE", "VIEW" },isInitColumn);
        return listTbls;
    }

    /**
     * 数据库下所有的数据表
     * 
     * @param types {"TABLE","VIEW"}
     * @return
     */
    public static List<TableMeta> getTables(Connection conn, String types[],boolean isInitColumn) {
        List<TableMeta> listTbls = getTablesByMeta(conn, types,isInitColumn);
        return listTbls;
    }
    

    /**
     * 返回数据库指定表的字段列
     * 
     * @param databaseMeta
     * @return
     * @throws Exception
     */
    public static List<ColumnMeta> getColumns(Connection conn, String tableName) {
        List<ColumnMeta> listCols = getColumnsByMeta(conn, tableName);
        return listCols;
    }

    /**
     * 通过DatabaseMeta 返回数据库下所有的数据表
     * 
     * @param types {"TABLE","VIEW"}
     * @return
     */
    public static List<TableMeta> getTablesByMeta(Connection conn, String types[],boolean isInitColumnList) {
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            
            String dbConfigname=ConnectionManager.getDbConfigName(conn);
            DatabaseConfig c=DatabaseConfigManager.getConfigMgr().get(dbConfigname);
            
            String scheme = getSchema(dbmd, c);
            // ResultSet rsTables = dbmd.getTables(conn.getCatalog(),
            // conn.getSchema(), null, types);
            String dbname="";
            try{
                dbname=conn.getCatalog();
            }catch(Exception e){
            }
             
//           if( dbname==null){
//                try{
//                    dbname=conn.getSchema();
//                }catch(Exception e){
//                }
//           }

            DatabaseMeta dbm=new DatabaseMeta(dbname,dbConfigname, c.getDatabaseType());
            
            ResultSet rsTables = dbmd.getTables(null, scheme, null, types);
            List<TableMeta> tables = new ArrayList<TableMeta>();
            while (rsTables.next()) {
                
              //  System.out.print(" TABLE_TYPE="+rsTables.getString("TABLE_TYPE"));
              //   System.out.print(" TABLE_CAT="+rsTables.getString("TABLE_CAT"));
              //  System.out.println(" TABLE_SCHEM="+rsTables.getString("TABLE_SCHEM"));
                
                TableMeta tableMeta = new TableMeta(dbm,rsTables.getString("TABLE_NAME"));
                tableMeta.setComment(rsTables.getString("REMARKS"));
                if(!PluginHelper.isChinese(tableMeta.getName()) && !containSpecialChar(tableMeta.getName())){
                    tables.add(tableMeta);
                }
                if(isInitColumnList){
                    try{
                        tableMeta.setColumns(getColumns(conn, tableMeta.getName()));
                    }catch(Exception e){
                        //donothing
                    }
                }
            }
            rsTables.close();
            return tables;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    private static String getSchema(DatabaseMetaData dbmd, DatabaseConfig c) throws SQLException {
        String scheme=null;
        if(DataBaseType.ORACLE.equals(c.getDatabaseType())){
            scheme=dbmd.getUserName();
        }
        return scheme;
    }
    /**
     * 返回数据库指定表的字段列
     * 
     * @param databaseMeta
     * @return
     * @throws Exception
     */
    public static List<ColumnMeta> getColumnsByMeta(Connection conn, String tableName) {
        try {
            
            
            DatabaseMetaData dbmd = conn.getMetaData();

            String dbConfigname=ConnectionManager.getDbConfigName(conn);
            DatabaseConfig c=DatabaseConfigManager.getConfigMgr().get(dbConfigname);
            String schema = getSchema(dbmd, c);
            
            // Verify if the database has catalogs
            String catalog = null;
            try {
               catalog = conn.getCatalog();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ResultSet rs = dbmd.getColumns(catalog, schema, tableName, null);
            List<ColumnMeta> columnMetas = new ArrayList<ColumnMeta>();
            while (rs.next()) {
                ColumnMeta columnMeta = new ColumnMeta();

                columnMeta.setName(rs.getString("COLUMN_NAME"));
                columnMeta.setComment(rs.getString("REMARKS"));
                columnMeta.setSqlType(rs.getInt("DATA_TYPE"));
                columnMeta.setTypeName(rs.getString("TYPE_NAME"));
                // 设置java类型
              //  columnMeta.setJavaType(CommonUtil.getClass(rs.getType()));

                columnMeta.setColumnSize(rs.getInt("COLUMN_SIZE"));
                columnMeta.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));
                columnMeta.setNullable(rs.getString("IS_NULLABLE"));

                columnMetas.add(columnMeta);
            }
            rs.close();
            setPrimaryKeys(columnMetas, dbmd, catalog, schema, tableName);
            setForeignKeys(columnMetas, dbmd, catalog, schema, tableName);
            return columnMetas;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 设置主键
     * 
     * @param columns
     * @param dbmd
     * @param catalog
     * @param schema
     * @throws SQLException
     */
    private static void setPrimaryKeys(List<ColumnMeta> columns, DatabaseMetaData dbmd, String catalog, String schema,
            String tableName) throws SQLException {
        // -- PRIMARY KEYS --
        ResultSet rsPk = dbmd.getPrimaryKeys(catalog, schema, tableName);
        while (rsPk.next()) {
            String pkName = (String) rsPk.getString("COLUMN_NAME");
            for (ColumnMeta column : columns) {
                if (column.getName().equalsIgnoreCase(pkName)) {
                    column.setPrimaryKey(true);
                }
            }
        }
        rsPk.close();
    }

    private static void setForeignKeys(List<ColumnMeta> columns, DatabaseMetaData dbmd, String catalog, String schema,
            String tableName) throws SQLException {
        // -- Foreign KEYS --
        ResultSet rsFK = dbmd.getImportedKeys(catalog, schema, tableName);
        while (rsFK.next()) {
            // String pkTable = (String) rsFK.getString("PKTABLE_NAME");
            // String pkColumn = (String) rsFK.getString("PKCOLUMN_NAME");
            String fkColumn = (String) rsFK.getString("FKCOLUMN_NAME");

            for (ColumnMeta column : columns) {
                if (column.getName().equalsIgnoreCase(fkColumn)) {
                    column.setForeignKey(true);
                }
            }
        }
        rsFK.close();
    }
    
    private  static boolean containSpecialChar(String tblName){
        char[] a=SpecialCharString.toCharArray();
        for (int j = 0; j <  SpecialCharString.length(); j++) {
            if(tblName.indexOf(a[j])>-1){
                return true;
            }
        }
       return false;
    }
    
    @SuppressWarnings("unused")
    private  static String replaceSpecialChar(String tbl){
        char[] a=SpecialCharString.toCharArray();
        for (char c : a) {
            tbl= tbl.replace(c, '_');
        }
       return tbl;
    }
   
}
