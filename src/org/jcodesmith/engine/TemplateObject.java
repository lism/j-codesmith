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
 * @Desc 模板对象
 * @author greki.shen
 * @date 2013年11月27日
 * @Version V1.0
 */
public class TemplateObject {

    private String templatePath;

    private List<TemplateProperty> propertyList;

    private ITemplateEngine templateEngine;

    public TemplateObject(String templatePath) {

        this.templatePath = templatePath;
        templateEngine = TemplateEngineFactory.getTemplateEngine(templatePath);
        propertyList = templateEngine.getTemplateProperties(templatePath);
        // loading default class

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
     * 
     * @return
     */
    public String merge() {
        Map<String, Object> context = new HashMap<String, Object>();
        StringWriter sw = new StringWriter();
        templateEngine.merge(templatePath, context, this, sw);
        String ret = sw.toString();

        // 删除 最前面的，property标签产生的空行
        String end = "\r\n";
        int s = 0;
        while (ret.startsWith(end, s)) {
            s = end.length() + s;
        }
        if (s > 0) {
            ret = ret.substring(s);
        }
        return ret;
    }

    /**
     * 设置属性，按顺序设置
     * 
     * @param value
     */
    public void setPropertys(Object... values) {
        int i = 0;
        for (TemplateProperty p : propertyList) {
            Class<?> c = p.getType().getClazz();
            Object v = values[i++];
            Class<?> vz = v.getClass();
            if (vz.isAssignableFrom(c)) {
                p.setValue(v);
            } else if (vz.isPrimitive()) {
                if (vz.getName().indexOf("boolean") > -1) {
                    p.setValue(Boolean.valueOf(v.toString()));
                } else if (vz.getName().indexOf("long") > -1) {
                    p.setValue(Long.valueOf(v.toString()));
                } else if (vz.getName().indexOf("double") > -1) {
                    p.setValue(Double.valueOf(v.toString()));
                }
            } else {
                throw new IllegalArgumentException("property:" + p.getName() + ",it's type error, property type is "
                        + c.getName() + ",but value type is" + v.getClass().getName());
            }
        }
    }

    /**
     * 设置属性值
     * 
     * @param name
     * @param value
     */
    public void setProperty(String name, Object value) {
        for (TemplateProperty p : propertyList) {
            if (p.getName().equals(name)) {
                Class<?> c = p.getType().getClazz();
                if (value.getClass().isAssignableFrom(c)) {
                    p.setValue(value);
                } else {
                    throw new IllegalArgumentException("property:" + p.getName()
                            + ",it's type error, property type is " + c.getName() + ",but value type is"
                            + value.getClass().getName());
                }
            }
        }
    }
}
