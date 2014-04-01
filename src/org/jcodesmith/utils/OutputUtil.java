package org.jcodesmith.utils;

import java.io.File;

import org.jcodesmith.engine.ExcuteTracker;
import org.jcodesmith.engine.TemplateObject;


public class OutputUtil {
    
    /**
     * 輸出文件
     * @param templatePath:全路径
     * @param savePath
     * @param propertValues
     */
    public static void write(String savePath,String templatePath, Object ... propertValues){

        if(FileUtil.isRelativePath(templatePath)){
            File f=new File(ExcuteTracker.getExcutingTemplateObject().getTemplatePath());
            templatePath= f.getParent()+"/"+templatePath;
        }
        TemplateObject tobj=new TemplateObject(templatePath);
        tobj.setPropertys(propertValues);
        FileUtil.write(savePath, tobj.merge());
    }
    
    /**
     * 輸出文件
     * @param templatePath:全路径
     * @param fullClassName 类全名
     * @param propertValues
     */
    public static void writeJava(String fullClassName,String templatePath, Object ... propertValues){

        String savePath=CommonUtil.package2path(fullClassName);
        write(savePath, templatePath, propertValues);
    }
    
    
}
