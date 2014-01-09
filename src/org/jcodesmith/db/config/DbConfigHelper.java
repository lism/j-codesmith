package org.jcodesmith.db.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jcodesmith.db.dal.DataBaseType;
import org.jcodesmith.plugin.helper.PluginHelper;

public class DbConfigHelper {
    private static Map<DataBaseType, DatabaseConfig> map = new HashMap<DataBaseType, DatabaseConfig>();
    static {
        DatabaseConfig mysql = new DatabaseConfig();
        mysql.setDatabaseType(DataBaseType.MYSQL);
        mysql.setDriverClazz("com.mysql.jdbc.Driver");
        mysql.setDriverJars(PluginHelper.getConfigFilePath("/lib/mysql-connector-5.1.6.jar"));
        mysql.setUrl("jdbc:mysql://192.168.1.15:3306/guser?user=guser&password=guser123&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false");

        map.put(DataBaseType.MYSQL, mysql);

        DatabaseConfig oracle = new DatabaseConfig();
        mysql.setDatabaseType(DataBaseType.ORACLE);
        oracle.setDriverClazz("oracle.jdbc.driver.OracleDriver");
        oracle.setDriverJars(PluginHelper.getConfigFilePath("/lib/ojdbc6.jar"));
        oracle.setUrl("jdbc:oracle:thin:p95169/lx95123@192.168.1.15:1521:orcl");

        map.put(DataBaseType.ORACLE, oracle);

    }

    public static String[] getDriveClasses() {
        String[] ret = new String[map.size()];
        int i = 0;
        for (Entry<DataBaseType, DatabaseConfig> entry : map.entrySet()) {
            ret[i] = entry.getValue().getDriverClazz();
            i++;
        }
        return ret;
    }

    public static DataBaseType getTypeByName(String name) {
        if (name == null) {
            return null;
        }
        for (Entry<DataBaseType, DatabaseConfig> entry : map.entrySet()) {
            if (entry.getKey().toString().toLowerCase().equals(name.toLowerCase())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public static DataBaseType getTypeByDriveClass(String driverName) {
        if (driverName == null) {
            return null;
        }
        for (Entry<DataBaseType, DatabaseConfig> entry : map.entrySet()) {
            if (entry.getValue().getDriverClazz().equals(driverName)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public static DatabaseConfig getDefaultDbConfig(DataBaseType dbtype){
        return map.get(dbtype);
    }
  
  public static String getJdbcUrlByDrive(String driveClass){
      if(driveClass==null){
          return null;
      }
      for (Entry<DataBaseType, DatabaseConfig> entry : map.entrySet()) {
          if (entry.getValue().getDriverClazz().equals(driveClass)) {
              return entry.getValue().getUrl();
          }
      }
      return null;
  }
  
  public static String getJarByDrive(String driveClass){
      if(driveClass==null){
          return null;
      }
      for (Entry<DataBaseType, DatabaseConfig> entry : map.entrySet()) {
          if (entry.getValue().getDriverClazz().equals(driveClass)) {
              return entry.getValue().getDriverJars();
          }
      }
      return null;
  }

}
