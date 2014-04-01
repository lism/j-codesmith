package org.jcodesmith.engine;

import java.util.HashMap;
import java.util.Map;

public class TemplateSavePathManager {
    private static Map<String, String> map=new HashMap<String, String>();
    public static void put(String templatePath,String savePath){
        map.put(templatePath, savePath);
    }
    
    public static String get(String templatePath){
       return  map.get(templatePath);
    }
}
