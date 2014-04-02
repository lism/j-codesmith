/*
 * Project: jcodesmith
 * 
 * File Created at 2014年4月1日
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
package org.jcodesmith.engine.helper;

import java.util.ArrayList;
import java.util.List;

import org.jcodesmith.db.meta.TableMeta;
import org.jcodesmith.engine.SupportType;
import org.jcodesmith.engine.TemplateProperty;
import org.jcodesmith.plugin.helper.OjbectSerialzer;
import org.jcodesmith.plugin.helper.PluginConfigFile;
import org.jcodesmith.plugin.helper.PluginSetting;
import org.jcodesmith.ui.template.views.TemplateExcuteData;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @Type ExcuteDialogDataCacheHelper
 * @Desc 模板执行的属性值设置
 * @author DELL
 * @date 2014年4月1日
 * @Version V1.0
 */
public class ExcuteDialogDataCacheHelper {
    // 模板路径+模板属性
    private static final PluginConfigFile<TemplateExcuteData> config= new PluginConfigFile<TemplateExcuteData>(
            PluginSetting.EXCUTE_DIALOG_DATA_CACHE, TemplateExcuteData.class,new OjbectSerialzer());
   
    //30天过期
    private final static long timeout=1000L*24*60*60*30;
    
    public static void load() {
         config.load();
         if(config.listAllKey()==null){
             return;
         }
         //删除过期
         long time=System.currentTimeMillis();
         for (String k : config.listAllKey()) {
             TemplateExcuteData d=config.get(k);
             if(d!=null && time-d.getTime()>timeout){
                 config.remove(d);
             }else{
                 //tempalteProject Object对象解析
                List<TemplateProperty> list= d.getTemplateProperties();
                if(list==null){
                    return ;
                }
                for (TemplateProperty p : list) {
                    if(p.getValue()==null){
                        return;
                    }
                    if( p.getValue() instanceof JSONObject ){
                        if(SupportType.TABLE.equals(p.getType())){
                         //   JSONObject.parseObject(((JSONObject)p.getValue()).toJSONString(), TableMeta.class)
                            p.setValue(JSONObject.toJavaObject((JSONObject)p.getValue(), TableMeta.class));
                        }
                    }else  if( p.getValue() instanceof JSONArray ){
                        
                         if(SupportType.TABLES.equals(p.getType())){
                             List<TableMeta> l=new ArrayList<TableMeta>(1);
                             p.setValue(  JSONObject.parseArray(((JSONArray)p.getValue()).toJSONString(),  l.getClass()));
                        }
                    }
                }
             }
             
             
         }
         
         
         
    }

    public static List<TemplateProperty> getPropertyList(String tempaltePath) {
        TemplateExcuteData data = config.get(tempaltePath);
        if (data == null) {
            return null;
        } else {
            return data.getTemplateProperties();
        }
    }

    public static TemplateProperty getTemplateProperty(String tempaltePath, String propertyName) {
        List<TemplateProperty> list = getPropertyList(tempaltePath);
        if (list != null) {
            for (TemplateProperty p : list) {
                if (p.getName().equals(propertyName)) {
                    return p;
                }
            }
        }
        return null;
    }

    public static void setProperty(String tempaltePath, List<TemplateProperty> list) {
        TemplateExcuteData d = new TemplateExcuteData();
        d.setPath(tempaltePath);
        d.setTemplateProperties(list);
        d.setTime(System.currentTimeMillis());
        config.put(d);
    }

    public static void saveCache() {
        config.saveToFile();
    }

}
