/**
 * 
 */
package org.jcodesmith.db.dal;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaCore;

/**
 * jdbc驱动管理
 * 
 * @author DELL
 * 
 */
public class DriverManager {

    private static Map<String, Driver> driverMap = new ConcurrentHashMap<String, Driver>();

    /**
     * eclipse 中M2_REPO的值
     */
    private static String M2_REPO = null;
    static {
        try {
            IPath path = (IPath) JavaCore.getClasspathVariable("M2_REPO");
            M2_REPO = path.toString();
        } catch (Throwable e) {
            e.printStackTrace();
            M2_REPO = "${M2_REPO}";
        }
    }

    /**
     * 分离jar文件路径
     * 
     * @param jars
     * @return
     */
    private static URL[] getStringList(String driverjar) {
    	List<URL> list = new ArrayList<URL>();
    	if (driverjar == null) {
    		return new URL[0];
    	}
    	String[] jars = driverjar.split("[;\n\r]");
    	if (jars == null || jars.length == 0) {
    		return new URL[0];
    	}
    	try {
    		for (String jar : jars) {
    			if (jar == null) {
    				continue;
    			}
    			String str = jar.trim();
    			if ("".equals(str)) {
    				continue;
    			}
    			str = str.replace("${M2_REPO}", M2_REPO);
    			URL url = new File(str).toURI().toURL();
    			list.add(url);
    		}
    		return list.toArray(new URL[0]);
    	} catch (MalformedURLException e) {
    		throw new RuntimeException(e);
    	}
    }

    /**
     * 获取驱动
     * 
     * @param dbName
     * @return
     */
    public static Driver getDriver(String dbName) {
        return driverMap.get(dbName);
    }

    /**
     * 获取db驱动
     * 
     * @param dbMate
     * @return
     */
    @SuppressWarnings("resource")
    public static Driver registerDriver(String name, String driverClass, String driverJarUrls, boolean refresh) {

        Driver driver = driverMap.get(name);
        if (!refresh) {
            if (driver != null) {
                return driver;
            }
        }
        URLClassLoader loader = null;
        try {
            URL[] driverUrls = getStringList(driverJarUrls);
            loader = new URLClassLoader(driverUrls);

            Class<?> driverClazz = loader.loadClass(driverClass);
            driver = (Driver) driverClazz.newInstance();

            if (driver == null) {
                throw new RuntimeException(driverClass + "driver class Invalid, please verify..");
            } else {
                driverMap.put(name, driver);
                return driver;
            }
        } catch (Exception e) {
            throw new RuntimeException(driverClass + "driver class Invalid, please verify..", e);
        } finally {
            if (loader != null) {
                // loader.close();
            }
        }
    }
}
