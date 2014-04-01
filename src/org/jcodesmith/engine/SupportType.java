/*
 * Project: jcodesmith
 * 
 * File Created at 2013年11月25日
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

import java.util.ArrayList;

import org.jcodesmith.db.meta.TableMeta;


/**
 * @Type SupportType
 * @Desc 
 * @author DELL
 * @date 2013年11月25日
 * @Version V1.0
 */
public enum SupportType {
    
    STRING("String"),BOOLEAN("boolean"),LONG("long"),DOUBLE("double"),TABLE("table"),TABLES("tables"),DIR("dir");
    
    private String type;
    private Class<?> clazz;
    
    SupportType(String type){
        this.type=type;
        if(type.equals("table")){
            clazz=TableMeta.class;
        }else   if(type.equals("tables")){
            clazz=ArrayList.class;
        }else if(type.toLowerCase().equals("string")){
            clazz=String.class;
        } else if(type.toLowerCase().equals("dir")){
            clazz=String.class;
        }
        else if(type.toLowerCase().equals("boolean")){
            clazz=Boolean.class;
        }
        else if(type.toLowerCase().equals("long")){
            clazz=Long.class;
        }
        else if(type.toLowerCase().equals("double")){
            clazz=Double.class;
        }else{
            throw new IllegalArgumentException("error type");
        }
    }
    



    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getType() {
        return type;
    }



    public void setType(String type) {
        this.type = type;
    }

   public static SupportType getEnum(String type){
       for (SupportType t : SupportType.values()) {
         if( t.getType().equals(type)){
              return t;
          }
       }
       throw new IllegalArgumentException("error type");
   }
}
