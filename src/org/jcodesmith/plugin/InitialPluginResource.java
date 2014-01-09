package org.jcodesmith.plugin;

import org.eclipse.core.runtime.IStatus;
import org.jcodesmith.plugin.helper.PluginHelper;
import org.jcodesmith.plugin.helper.PluginLogger;

/**
 * 
 * @Type InitialPluginResource
 * @Desc 初始化
 * @author DELL
 * @date 2014年1月8日
 * @Version V1.0
 */
public class InitialPluginResource {
    
    static String[] res={
        "/lib/mysql-connector-5.1.6.jar",
        "/lib/ojdbc6.jar",
        "resource/ftl/DO.ftl",
        "resource/ftl/ibatis.xml.ftl"
    };
    
    public static void resouceInitial(){
        try{
        for (String p : res) {
            PluginHelper.initResourceFile(p);
        }
        }catch(Exception e){
            e.printStackTrace();
            PluginLogger.log(e.getMessage(), IStatus.ERROR);
        }
    }
    
    
}
