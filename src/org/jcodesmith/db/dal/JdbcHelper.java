package org.jcodesmith.db.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库访问帮助类
 * 
 * @author greki.shen
 * 
 */
public class JdbcHelper {

	/**
	 * 用于查询，返回结果集
	 * 
	 * @param sql
	 *            sql语句
	 * @return 结果集
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public static List query(Connection conn, String sql) throws SQLException {

		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = getPreparedStatement(conn, sql);
			rs = preparedStatement.executeQuery();

			return ResultToListMap(rs);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(conn, preparedStatement, rs);
		}

	}

	/**
	 * 用于带参数的查询，返回结果集
	 * 
	 * @param sql
	 *            sql语句
	 * @param paramters
	 *            参数集合
	 * @return 结果集
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public static List query(Connection conn, String sql, Object... paramters)
			throws SQLException {

		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = getPreparedStatement(conn, sql);

			for (int i = 0; i < paramters.length; i++) {
				preparedStatement.setObject(i + 1, paramters[i]);
			}
			rs = preparedStatement.executeQuery();
			return ResultToListMap(rs);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(conn, preparedStatement, rs);
		}
	}

	/**
	 * 返回单个结果的值，如count\min\max等等
	 * 
	 * @param sql
	 *            sql语句
	 * @return 结果集
	 * @throws SQLException
	 */
	public static Object getSingle(Connection conn, String sql)
			throws SQLException {
		Object result = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = getPreparedStatement(conn, sql);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = rs.getObject(1);
			}
			return result;
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(conn, preparedStatement, rs);
		}

	}

	/**
	 * 返回单个结果值，如count\min\max等
	 * 
	 * @param sql
	 *            sql语句
	 * @param paramters
	 *            参数列表
	 * @return 结果
	 * @throws SQLException
	 */
	public static Object getSingle(Connection conn, String sql,
			Object... paramters) throws SQLException {
		Object result = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = getPreparedStatement(conn, sql);

			for (int i = 0; i < paramters.length; i++) {
				preparedStatement.setObject(i + 1, paramters[i]);
			}
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = rs.getObject(1);
			}
			return result;
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(conn, preparedStatement, rs);
		}
	}

	/**
	 * 用于增删改
	 * 
	 * @param sql
	 *            sql语句
	 * @return 影响行数
	 * @throws SQLException
	 */
	public static int update(Connection conn, String sql) throws SQLException {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = getPreparedStatement(conn, sql);

			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(conn, preparedStatement, null);
		}
	}

	/**
	 * 用于增删改（带参数）
	 * 
	 * @param sql
	 *            sql语句
	 * @param paramters
	 *            sql语句
	 * @return 影响行数
	 * @throws SQLException
	 */
	public static int update(Connection conn, String sql, Object... paramters)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = getPreparedStatement(conn, sql);

			for (int i = 0; i < paramters.length; i++) {
				preparedStatement.setObject(i + 1, paramters[i]);
			}
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(conn, preparedStatement, null);
		}
	}

	/**
	 * 插入值后返回主键值
	 * 
	 * @param sql
	 *            插入sql语句
	 * @return 返回结果
	 * @throws Exception
	 */
	public static Object insertWithReturnPrimeKey(Connection conn, String sql)
			throws SQLException {
		ResultSet rs = null;
		Object result = null;
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.execute();
			rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				result = rs.getObject(1);
			}
			return result;
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 插入值后返回主键值
	 * 
	 * @param sql
	 *            插入sql语句
	 * @param paramters
	 *            参数列表
	 * @return 返回结果
	 * @throws SQLException
	 */
	public static Object insertWithReturnPrimeKey(Connection conn, String sql,
			Object... paramters) throws SQLException {
		ResultSet rs = null;
		Object result = null;
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < paramters.length; i++) {
				preparedStatement.setObject(i + 1, paramters[i]);
			}
			preparedStatement.execute();
			rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				result = rs.getObject(1);
			}
			return result;
		} catch (SQLException e) {
			throw new SQLException(e);
		}

	}

	/**
	 * 调用存储过程执行查询
	 * 
	 * @param procedureSql
	 *            存储过程
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public static List callableQuery(Connection conn, String procedureSql)
			throws SQLException {
		ResultSet rs = null;
		CallableStatement callableStatement = null;
		try {
			callableStatement = getCallableStatement(conn, procedureSql);
			rs = callableStatement.executeQuery();
			return ResultToListMap(rs);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(conn, callableStatement, rs);
		}
	}

	/**
	 * 调用存储过程（带参数）,执行查询
	 * 
	 * @param procedureSql
	 *            存储过程
	 * @param paramters
	 *            参数表
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public static List callableQuery(Connection conn, String procedureSql,
			Object... paramters) throws SQLException {
		ResultSet rs = null;
		CallableStatement callableStatement = null;
		try {
			callableStatement = getCallableStatement(conn, procedureSql);

			for (int i = 0; i < paramters.length; i++) {
				callableStatement.setObject(i + 1, paramters[i]);
			}
			rs = callableStatement.executeQuery();
			return ResultToListMap(rs);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(conn, callableStatement, rs);
		}
	}

	/**
	 * 调用存储过程，查询单个值
	 * 
	 * @param procedureSql
	 * @return
	 * @throws SQLException
	 */
	public static Object callableGetSingle(Connection conn, String procedureSql)
			throws SQLException {
		Object result = null;
		ResultSet rs = null;
		CallableStatement callableStatement = null;
		try {
			callableStatement = getCallableStatement(conn, procedureSql);
			rs = callableStatement.executeQuery();
			while (rs.next()) {
				result = rs.getObject(1);
			}
			return result;
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(conn, callableStatement, rs);
		}
	}

	/**
	 * 调用存储过程(带参数)，查询单个值
	 * 
	 * @param procedureSql
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public static Object callableGetSingle(Connection conn,
			String procedureSql, Object... paramters) throws SQLException {
		Object result = null;
		ResultSet rs = null;
		CallableStatement callableStatement = null;
		try {
			callableStatement = getCallableStatement(conn, procedureSql);

			for (int i = 0; i < paramters.length; i++) {
				callableStatement.setObject(i + 1, paramters[i]);
			}
			rs = callableStatement.executeQuery();
			while (rs.next()) {
				result = rs.getObject(1);
			}
			return result;
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(conn, callableStatement, rs);
		}
	}

	public static Object callableWithParamters(Connection conn,
			String procedureSql) throws SQLException {
		CallableStatement callableStatement = null;
		try {
			callableStatement = getCallableStatement(conn, procedureSql);
			callableStatement.registerOutParameter(0, Types.OTHER);
			callableStatement.execute();
			return callableStatement.getObject(0);

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(conn, callableStatement);
		}

	}

	/**
	 * 调用存储过程，执行增删改
	 * 
	 * @param procedureSql
	 *            存储过程
	 * @return 影响行数
	 * @throws SQLException
	 */
	public static int callableUpdate(Connection conn, String procedureSql)
			throws SQLException {
		CallableStatement callableStatement = null;
		try {
			callableStatement = getCallableStatement(conn, procedureSql);
			return callableStatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(conn, callableStatement);
		}
	}

	/**
	 * 调用存储过程（带参数），执行增删改
	 * 
	 * @param procedureSql
	 *            存储过程
	 * @param parameters
	 * @return 影响行数
	 * @throws SQLException
	 */
	public static int callableUpdate(Connection conn, String procedureSql,
			Object... parameters) throws SQLException {
		CallableStatement callableStatement = null;
		try {
			callableStatement = getCallableStatement(conn, procedureSql);
			for (int i = 0; i < parameters.length; i++) {
				callableStatement.setObject(i + 1, parameters[i]);
			}
			return callableStatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(conn, callableStatement, null);
		}
	}

	/**
	 * 批量更新数据
	 * 
	 * @param sqlList
	 *            一组sql
	 * @return
	 */
	public static int[] batchUpdate(Connection conn, List<String> sqlList) {

		int[] result = new int[] {};
		Statement statenent = null;
		try {
			conn.setAutoCommit(false);
			statenent = conn.createStatement();
			for (String sql : sqlList) {
				statenent.addBatch(sql);
			}
			result = statenent.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new ExceptionInInitializerError(e1);
			}
			throw new ExceptionInInitializerError(e);
		} finally {
			free(conn, statenent, null);
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List ResultToListMap(ResultSet rs) throws SQLException {
		List list = new ArrayList();
		while (rs.next()) {
			ResultSetMetaData md = rs.getMetaData();
			Map map = new HashMap();
			for (int i = 1; i < md.getColumnCount(); i++) {
				map.put(md.getColumnLabel(i), rs.getObject(i));
			}
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 测试连接
	 * @param conn
	 * @return
	 */
    public static boolean testConnection(Connection conn) throws SQLException{
            JdbcHelper.query(conn, "select 1 from DUAL");
            return true;
    }

	/**
	 * 获取PreparedStatement
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	private static PreparedStatement getPreparedStatement(Connection conn,
			String sql) throws SQLException {
		return conn.prepareStatement(sql);
	}

	/**
	 * 获取CallableStatement
	 * 
	 * @param procedureSql
	 * @throws SQLException
	 */
	private static CallableStatement getCallableStatement(Connection conn,
			String procedureSql) throws SQLException {
		return conn.prepareCall(procedureSql);
	}

	/**
	 * 释放资源
	 * 
	 * @param rs
	 *            结果集
	 */
	public static void free(Connection conn, ResultSet rs) {

		JdbcUtils.free(conn, null, rs);
	}

	/**
	 * 释放资源
	 * 
	 * @param rs
	 *            结果集
	 */
	public static void free(Connection conn, Statement statement) {

		JdbcUtils.free(conn, statement, null);
	}

	/**
	 * 释放资源
	 * 
	 * @param statement
	 * @param rs
	 */
	public static void free(Connection conn, Statement statement, ResultSet rs) {
		JdbcUtils.free(conn, statement, rs);
	}
	
}
