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


/**
 * @Type SupportType
 * @Desc 
 * @author DELL
 * @date 2013年11月25日
 * @Version V1.0
 */
public enum SupportType {
    
    STRING("String"),BOOLEAN("boolean"),LONG("long"),DOUBLE("double"),TABLE("table"),TABLES("tables");
    
    private String javaType;
    private String type;
    
    SupportType(String type){
    
        this.type=type;
        if(type.equals("table")){
            javaType="org.jcodesmith.db.meta.TableMeta";
        }else   if(type.equals("tables")){
            javaType="java.util.ArrayList.ArrayList<org.jcodesmith.db.meta.TableMeta>";
        }else{
            javaType=type;
        }
    }
    


    public String getJavaType() {
        return javaType;
    }



    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }



    public String getType() {
        return type;
    }



    public void setType(String type) {
        this.type = type;
    }



    /**
     * 获取支持的JAVA类型
     * @return
     */
    public static String[] getSupportTypes(){
        String[] ret=new String[SupportType.values().length];
        int i=0;
        for (SupportType t : SupportType.values()) {
            ret[i]=t.javaType;
            i++;
        }
        return ret;
    }
}
