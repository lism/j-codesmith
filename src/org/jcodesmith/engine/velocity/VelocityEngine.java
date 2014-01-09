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
package org.jcodesmith.engine.velocity;

import java.io.StringWriter;
import java.util.List;

import org.jcodesmith.engine.ITemplateEngine;
import org.jcodesmith.engine.TemplateObject;
import org.jcodesmith.engine.TemplateProperty;

/**
 * @Type VelocityEngine
 * @Desc 基于velocity的模板引擎
 * @author greki.shenwr
 * @date 2013年11月11日
 * @Version V1.0
 */
public class VelocityEngine implements ITemplateEngine {

    
    public VelocityEngine(){
        
    }
    /* (non-Javadoc)
     * @see org.jcodesmith.engine.ITemplateEngine#merge(java.lang.Object, java.io.StringWriter)
     */
    @Override
    public void merge(String templatePath,Object context, StringWriter wr) {
//        VelocityEngine velocityEngine = new VelocityEngine();
//        192         velocityEngine.setProperty("input.encoding", "UTF-8");
//        193         velocityEngine.setProperty("output.encoding", "UTF-8");
//        194         velocityEngine.init();
//        195         Template template = velocityEngine.getTemplate(fileVMPath);
//        196         VelocityContext velocityContext = new VelocityContext();
//        197         velocityContext.put("bean", bean);
//        198         velocityContext.put("annotation", annotation);
//        199         StringWriter stringWriter = new StringWriter();
//        200         template.merge(velocityContext, stringWriter);
//        201         return stringWriter.toString();
    }

    @Override
    public List<TemplateProperty> getTemplateProperties(String templatePath) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void merge(String templatePath, Object context, TemplateObject templateObj, StringWriter wr) {
        // TODO Auto-generated method stub
        
    }


}
