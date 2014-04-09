/*
 * Project: jcodesmith
 * 
 * File Created at 2013年12月25日
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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jcodesmith.engine.freemarker.PropertyFunction;
import org.jcodesmith.utils.CommonUtil;
import org.jcodesmith.utils.OutputUtil;

/**
 * @Type BuildInVariables
 * @Desc 模板共享变量,内建变量
 * @author DELL
 * @date 2013年12月25日
 * @Version V1.0
 */
public class BuildInVariables {
    /**
     * 指向模板对象自己
     */
    public static String TEMPALTE_OBJECT_NAME="this";
    
    private static Map<String, Object> map = new HashMap<String, Object>();
    static{
        map.put("property", new PropertyFunction());
        map.put("comUtil", new CommonUtil());
        map.put("outUtil", new OutputUtil());
    }
    private BuildInVariables() {

    }

    public static void put(String key, Object val) {
        map.put(key, val);
    }

    public static Object get(String key) {
        return map.get(key);
    }
    
    public static Set<String> keySet(){
        return map.keySet();
    }
    
    public static Set<Entry<String, Object>> entrySet(){
        return map.entrySet();
    }

}
