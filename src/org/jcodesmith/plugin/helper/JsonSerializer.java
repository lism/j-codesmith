/*
 * Project: jcodesmith
 * 
 * File Created at 2014年4月2日
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
package org.jcodesmith.plugin.helper;

import com.alibaba.fastjson.JSONObject;

/**
 * @Type JsonSerializer
 * @Desc  
 * @author DELL
 * @date 2014年4月2日
 * @Version V1.0
 */
public class JsonSerializer implements Serializer {
    
    private Class<?>  dataClazz;
    
    
    public JsonSerializer(Class<?>  dataClazz){
        this.dataClazz=dataClazz;
    }
    public void setDataClazz(Class<?> dataClazz) {
        this.dataClazz = dataClazz;
    }

    @Override
    public String wirteObject(Object obj) {
        return  JSONObject.toJSONString(obj);
    }

    @Override
    public Object readObject(String str) {
        return JSONObject.parseArray(str, dataClazz);
    }

}
