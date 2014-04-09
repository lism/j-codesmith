/*
 * Project: jcodesmith
 * 
 * File Created at 2013年11月11日
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
package org.jcodesmith.engine.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.jcodesmith.engine.CustomVariableManager;
import org.jcodesmith.engine.ExcuteTracker;
import org.jcodesmith.engine.ITemplateEngine;
import org.jcodesmith.engine.BuildInVariables;
import org.jcodesmith.engine.TemplateObject;
import org.jcodesmith.engine.TemplateProperty;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * @Type FreemarkEngine
 * @Desc 基于freemark的模板引擎
 * @author greki.shen
 * @date 2013年11月11日
 * @Version V1.0
 */
public class FreemarkEngine implements ITemplateEngine {
 
    static Configuration configuration=new Configuration();
    static{
        
        for (Entry<String, Object> en : BuildInVariables.entrySet()) {
                try {
                    configuration.setSharedVariable(en.getKey(), en.getValue());
                } catch (TemplateModelException e) {
                    e.printStackTrace();
                }
        }
        
        // 设置模版的文件夹路径，本人在src下新建了一个ftl文件夹
        // configuration.setClassForTemplateLoading(this.getClass(), "/ftl");
        configuration.setEncoding(Locale.CHINESE, "UTF-8");
        TemplateLoader loader=new FreeMarkerTemplateloader();
        configuration.setTemplateLoader(loader);
        
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.jcodesmith.engine.ITemplateEngine#merge(java.lang.Object,
     * java.io.StringWriter)
     */
    @Override
    public  void  merge(String templatePath, Object context, StringWriter wr) {
       
            org.jcodesmith.engine.TemplateObject tempobj=new org.jcodesmith.engine.TemplateObject(templatePath);
            merge(templatePath, context, tempobj, wr);
    }

    /**
     * 根据模版获得一个指定的模版
     * 
     * @param name
     * @return
     * @throws IOException 
     */
    public static Template getTemplate(String templatePath) throws IOException {
            // 更具名字获得指定的一个模版
            Template template = configuration.getTemplate(templatePath);
            return template;
    }

    @Override
    public  List<TemplateProperty> getTemplateProperties(String templatePath) {
        List<TemplateProperty> list=new ArrayList<TemplateProperty>();
        try {
            Template template=getTemplate(templatePath);
            String s=  template.toString();
            StringTokenizer st=new StringTokenizer(s, "<>", false);
            int  i=-1;
            while(st.hasMoreTokens()) { 
                String tk=st.nextToken();
                i=tk.indexOf(PropertyFunction.PROPERTY_TAG);
                if(i==0){
                    //find property tag
                    TemplateProperty property=PropertyFunction. getProperty(tk);
                    list.add(property);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return list;
    }

    @Override
    public void merge(String templatePath, Object context, org.jcodesmith.engine.TemplateObject templateObj, StringWriter wr) {
        
        //注入属性内容
        mergeContext(context,templateObj);
        //合并模板输出
        try {
            Template template=getTemplate(templatePath);
            ExcuteTracker.setExcutingTemplateObject(templateObj);
            template.process(context, wr);
            //ExcuteTracker.setExcutingTemplateObject(null);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
 @SuppressWarnings("unchecked")
   private void mergeContext(Object context, org.jcodesmith.engine.TemplateObject templateObj){
        if(context==null){
            context=new HashMap<String, Object>();
        }
        if(context instanceof Map){
            //属性变量
            Map<Object, Object> map=(Map<Object, Object>)context;
            for (TemplateProperty p : templateObj.getPropertyList()) {
                map.put(p.getName(), p.getValue());
            }
            
            //this变量
            map.put(BuildInVariables.TEMPALTE_OBJECT_NAME, templateObj);
            
            //自定义变量
            for (Entry<String, Object> e : CustomVariableManager.variableInstanceEntrySet()) {
                map.put(e.getKey(), e.getValue());
            }
            
        }
    }
}
