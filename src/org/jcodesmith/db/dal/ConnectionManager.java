/**
 * 
 */
package org.jcodesmith.db.dal;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.jcodesmith.db.config.DatabaseConfig;

/**
 * @author DELL
 * 
 */
public class ConnectionManager {

    public static Map<String, Connection> connMap = new ConcurrentHashMap<String, Connection>();

    public static void freeAllConnection() {
        for (Entry<String, Connection> con : connMap.entrySet()) {
            try {
                con.getValue().close();
            } catch (Exception e) {
            }
        }
    }

    private static boolean isConnectionValid(Connection conn) {
        try {
            return conn.isValid(10);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 释放连接
     * 
     * @param conn
     */
    public static void freeConnetion(Connection conn) {
        // if (conn == null) {
        // return;
        // }
        // try {
        // conn.close();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // for (Entry<String, Connection> c : map.entrySet()) {
        // if (conn.equals(c.getValue())) {
        // map.remove(c.getKey());
        // }
        // }
    }

    public static String getDbConfigName(Connection conn) {
        for (Entry<String, Connection> c : connMap.entrySet()) {
            if (c.getValue().equals(conn)) {
                return c.getKey();
            }
        }
        return null;
    }

    /**
     * 
     * @param dbConfigname 配置的db名字
     * @param reconnect
     * @return
     */
    public static Connection getConnection(String dbConfigname, String url) {
        // 从已有的连接里获取连接
        Connection conn = connMap.get(dbConfigname);
        if (conn != null) {
            if (isConnectionValid(conn)) {
                return conn;
            }
        }
        Driver driver = DriverManager.getDriver(dbConfigname);
        if (driver == null) {
            throw new RuntimeException("driver Invalid, please reload driver.");
        }
        Properties props =new Properties();
        props.put("remarksReporting","true");
        try {
            conn = driver.connect(url, props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (conn == null) {
            throw new RuntimeException("Connection Invalid, please verify URL.");
        } else {
            connMap.put(dbConfigname, conn);
            return conn;
        }
    }

    /**
     * 获取连接
     * 
     * @param dbMate
     * @return
     */
    public static Connection getConnection(DatabaseConfig dataConfig, boolean reconnect) {

        if(dataConfig==null){
            new RuntimeException("dataConfig is null.");
        }
        // 从已有的连接里获取连接
        Connection conn = connMap.get(dataConfig.getManulName());
        if (!reconnect) {
            if (conn != null) {
                if (isConnectionValid(conn)) {
                    return conn;
                }
            }
        }
        Driver driver = DriverManager.registerDriver(dataConfig.getManulName(), dataConfig.getDriverClazz(),
                dataConfig.getDriverJars(), reconnect);
        if (driver == null) {
            throw new RuntimeException("driver Invalid, please reload driver.");
        }

        // Properties props = new Properties();
        // props.put("user", username);
        // props.put("password", password);
        Properties props =new Properties();
        props.put("remarksReporting","true");
        try {
            conn = driver.connect(dataConfig.getUrl(), props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (conn == null) {
            throw new RuntimeException("Connection Invalid, please verify URL.");
        } else {
            connMap.put(dataConfig.getManulName(), conn);
            dataConfig.setConnection(conn);
            return conn;
        }
    }
}
