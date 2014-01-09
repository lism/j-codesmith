package org.jcodesmith.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Type IFreemarkController
 * @Desc 后台代码的Controller
 * @author DELL
 * @date 2013年11月13日
 * @Version V1.0
 */
public abstract class AbstractFreemarkController implements IController {

    /**
     * 修改执行模板的上下文
     * 
     * @param context
     */
    @SuppressWarnings("unchecked")
    @Override
    public void Run(Object context) {
        if (context == null) {
            Map<String, Object> model = new HashMap<String, Object>();
            excute(model);
        } else {
            excute((Map<String, Object>) context);
        }
    }

    /**
     * 修改执行模板的上下文
     * 
     * @param context
     */
    public abstract void excute(Map<String, Object> model);

}
