/*
 * Project: jcodesmith
 * 
 * File Created at 2013年11月22日
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

import java.io.Serializable;

/**
 * @Type TemplateProperty
 * @Desc 
 * @author DELL
 * @date 2013年11月22日
 * @Version V1.0
 */
public class TemplateProperty implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 798860056246135200L;
    private String name;
    private SupportType type;
    private Object value;
    private String defaultValue;
    private String description;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public SupportType getType() {
        return type;
    }
    public void setType(SupportType type) {
        this.type = type;
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    public String getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
