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
package org.jcodesmith.engine;

import java.io.StringWriter;
import java.util.List;

/**
 * @Type ITemplateEngine
 * @Desc 模板引擎
 * @author greki.shenwr
 * @date 2013年11月11日
 * @Version V1.0
 */
public interface ITemplateEngine {
    /**
     * 合并模板输出
     * @param context
     * @param wr
     * @return
     */
    public  void merge(String templatePath,Object context,StringWriter wr);
    

    /**
     *  合并模板输出
     * @param templatePath
     * @param context
     * @param property
     * @param wr
     */
    public void merge(String templatePath,Object context,org.jcodesmith.engine.TemplateObject templateObj,StringWriter wr);
    
    /**
     * 
     * @param templateUri
     * @return
     */
    public  List<TemplateProperty> getTemplateProperties(String templatePath);
    
    
}
