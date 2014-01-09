/**
 * 
 */
package org.jcodesmith.db.dal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author DELL jdbc工具类
 */
public class JdbcUtils {

	
	/**
	 * 建立数据库连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String dbname,String connectStr)
			throws SQLException {
		Connection conn = null;
		conn = ConnectionManager.getConnection(dbname,connectStr);
		return conn;
	}

	/**
	 * 释放连接
	 * 
	 * @param conn
	 */
	private static void freeConnection(Connection conn) {
	    ConnectionManager.freeConnetion(conn);
	}

	/**
	 * 释放statement
	 * 
	 * @param statement
	 */
	private static void freeStatement(Statement statement) {
		try {
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 释放resultset
	 * 
	 * @param rs
	 */
	private static void freeResultSet(ResultSet rs) {
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 释放资源
	 * 
	 * @param conn
	 * @param statement
	 * @param rs
	 */
	public static void free(Connection conn, Statement statement, ResultSet rs) {
		if (rs != null) {
			freeResultSet(rs);
		}
		if (statement != null) {
			freeStatement(statement);
		}
		if (conn != null) {
			freeConnection(conn);
		}
	}
}
