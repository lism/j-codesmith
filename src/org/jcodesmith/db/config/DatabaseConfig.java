package org.jcodesmith.db.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.jcodesmith.db.dal.DataBaseType;
import org.jcodesmith.plugin.helper.ConfigFileData;
import org.jcodesmith.utils.FileUtil;

/**
 * 数据库连接配置信息
 * 
 * @author greki.shen
 * 
 */
public class DatabaseConfig implements ConfigFileData {

	private String manulName = "";
	/**
	 * 数据库类型
	 */
	private DataBaseType databaseType;
	/**
	 * 连接url
	 */
	private String url = "";
	/**
	 * driver class
	 */
	private String driverClazz = "";

	/**
	 * driver jar路径名称，以;分割
	 */
	private String driverJars = "";

	public DatabaseConfig() {

	}

	/**
	 * 从properties文件中解析
	 * 
	 * @param propFile
	 */
	public DatabaseConfig(String propFile) {
		Properties p = FileUtil.loadProperties(propFile);

		this.setUrl(p.getProperty("database.url", ""));

		this.setDriverClazz(p.getProperty("database.driverClassName", ""));
		this.setDriverJars(p.getProperty("database.driverJar", ""));
	}


	private Connection connection = null;

	/**
	 * 数据库处于连接状态
	 * 
	 * @return
	 */
	public final boolean isConnected() {
		try {
			return connection != null && connection.isClosed() == false;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
	 * 识别的名称
	 */
	public String getManulName() {
			return this.manulName;
	}

	public void setManulName(String manulName) {
		this.manulName = manulName;
	}


	public DataBaseType getDatabaseType() {
	    if(databaseType==null && driverClazz!=null){
	        databaseType=DbConfigHelper.getTypeByDriveClass(driverClazz);
	    }
		return databaseType;
	}

	public String getDatabaseTypeStr() {
		return databaseType == null ? "" : databaseType.name();
	}

	public void setDatabaseType(DataBaseType databaseType) {
		this.databaseType = databaseType;
	}

	public void setDatabaseType(String name) {
		this.databaseType = DbConfigHelper.getTypeByName(name);
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriverClazz() {
		return driverClazz;
	}

	public void setDriverClazz(String driverClazz) {
		this.driverClazz = driverClazz;
	}

	public String getDriverJars() {
		return driverJars;
	}

	public void setDriverJars(String driverJars) {
		this.driverJars = driverJars.replace(';', '\n');
	}

    @Override
    public String getkey() {
        return getManulName();
    }

}
