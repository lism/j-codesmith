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
package org.jcodesmith.engine.factory;

import java.util.HashMap;
import java.util.Map;

import org.jcodesmith.engine.ITemplateEngine;
import org.jcodesmith.engine.freemarker.FreemarkEngine;
import org.jcodesmith.engine.velocity.VelocityEngine;

/**
 * @Type TemplateEngineFactory
 * @Desc
 * @author greki.shenwr
 * @date 2013年11月11日
 * @Version V1.0
 */
public class TemplateEngineFactory {

    private static final String FTL = ".ftl";
    private static final String VM = ".vm";
    private static Map<String, ITemplateEngine> engineMap = new HashMap<String, ITemplateEngine>();
    static {
        VelocityEngine v=new VelocityEngine();
        engineMap.put(VM, v);
        
         FreemarkEngine f=new FreemarkEngine();
        engineMap.put(FTL, f);
    }
    
    public static boolean isValidExtension(String name){
        if(name==null|| name.isEmpty()){
            return false;
        }
        name=name.toLowerCase();
        if(name.indexOf(FTL)>-1 || name.indexOf(VM)>-1){
            return true;
        }
        return false;
    }

    public static ITemplateEngine getTemplateEngine(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("template path is empty");
        }
        path = path.toLowerCase();
        if (path.endsWith(VM)) {
            return engineMap.get(VM);
        } else if (path.endsWith(FTL)) {
            return engineMap.get(FTL);
        }
        throw new IllegalArgumentException("template engine was not found");
    }

}
