/*
 * Project: jcodesmith
 * 
 * File Created at 2013年11月13日
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
 * @Type IController
 * @Desc 模板后台代码
 * @author greki.shen
 * @date 2013年11月13日
 * @Version V1.0
 */
public interface IController {

    /**
     * 修改执行模板的上下文
     * @param context
     */
        public void Run(Object context);
}
