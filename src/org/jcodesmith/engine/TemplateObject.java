/*
 * Project: jcodesmith
 * 
 * File Created at 2013年11月27日
 * 
 * Copyright 2012 Greenline.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Greenline Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Greenline.com.
 */
package org.jcodesmith.engine;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jcodesmith.engine.factory.TemplateEngineFactory;

/**
 * @Type TemplateObject
 * @Desc  模板对象
 * @author greki.shen
 * @date 2013年11月27日
 * @Version V1.0
 */
public class TemplateObject {
    
    private String templatePath;
    
    private List<TemplateProperty> propertyList;
    
    private ITemplateEngine templateEngine ;
    
    public TemplateObject(String templatePath){
        
        this.templatePath=templatePath;
        templateEngine = TemplateEngineFactory.getTemplateEngine(templatePath);
        propertyList = templateEngine.getTemplateProperties(templatePath);
        //loading default  class
        
    }
    public List<TemplateProperty> getPropertyList() {
        return propertyList;
    }
    
    public ITemplateEngine getTemplateEngine() {
        return templateEngine;
    }
    
    
    public String getTemplatePath() {
        return templatePath;
    }
    
    /**
     * 合并输出
     * @return
     */
    public String merge(){
        Map<String,Object> context=new HashMap<String,Object>();
        StringWriter sw=new StringWriter();
        templateEngine.merge(templatePath, context,this, sw);
        String ret=sw.toString();
       
        //删除 最前面的，property标签产生的空行
        String end="\r\n";
        int s=0;
        while(ret.startsWith(end,s)){
            s=end.length()+s;
        }
        if(s>0){
            ret=ret.substring(s);
        }
        return ret;
    }
    
}
