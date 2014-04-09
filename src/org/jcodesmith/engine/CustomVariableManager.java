package org.jcodesmith.engine;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CustomVariableManager {
    
    public static    List<CustomVariable> varList=new ArrayList<CustomVariable>();
    
    public static List<String> jarList= new ArrayList<String>();
    
    public static ConcurrentHashMap<String, Object> varMap=new ConcurrentHashMap<String, Object>();
    
    public static List<String> getJarList() {
        return jarList;
    }
    public static void add(String jarUrl){
        jarList.add(jarUrl);
    }

    public static List<CustomVariable> getVarList() {
        return varList;
    }

    public static void setJarList(List<String> flist) {
        jarList .clear();
        jarList.addAll(flist);
    }
    
    public static void setVarList(List<CustomVariable> flist) {
        varList .clear();
        varList.addAll(flist);
    }
    
    public static void add(CustomVariable var){
        varList.add(var);
    }
    
    public static void remove(CustomVariable var){
        varList.add(var);
    }
    
    public static void clear(){
        varList.clear();
        jarList.clear();
    }
    
    public static Set<Entry<String, Object>>  variableInstanceEntrySet(){
        
        Set<String> keys=varMap.keySet();
        for (String key : keys) {
           if(!varListContain(key)){
               varMap.remove(key);
           }
        }
        
        for (CustomVariable var : varList) {
            if (varMap.containsKey(var.name)) {
                continue;
            }else{
                Object varobj=createInstance(var.getClassName());
                if(varobj!=null){
                    varMap.put(var.getName(), varobj);
                }
            }
        }
        return varMap.entrySet();
    }
   private static boolean varListContain(String key){
       for (CustomVariable var : varList) {
           if(var.getName().equals(key)){
               return true;
           }
       }
       return false;
   }
    @SuppressWarnings("resource")
    private static Object createInstance(String className){
        URLClassLoader loader = null;
        try {
            URL[] jarUrls = getURLS();
            loader = new URLClassLoader(jarUrls);
            
            Class<?> clazz = loader.loadClass(className);
           Object  driver = clazz.newInstance();

            if (driver == null) {
                throw new RuntimeException(className + "  class Invalid, please verify..");
            } else {
                return driver;
            }
        } catch (Exception e) {
            throw new RuntimeException(className + " class Invalid, please verify..", e);
        } finally {
            if (loader != null) {
                // loader.close();
            }
        }
    }
    /**
     * 分离jar文件路径
     * 
     * @param jars
     * @return
     */
    private static URL[] getURLS() {
        List<URL> list = new ArrayList<URL>();
        try {
            for (String jar : jarList) {
                if (jar == null) {
                    continue;
                }
                String str = jar.trim();
                if ("".equals(str)) {
                    continue;
                }
                URL url = new File(str).toURI().toURL();
                list.add(url);
            }
            return list.toArray(new URL[0]);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    
    
    
}
