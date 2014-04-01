package org.jcodesmith.engine;

import org.jcodesmith.engine.TemplateObject;

public class ExcuteTracker {
    //正在执行的模板
    protected static  TemplateObject excutingTemplateObject;

    public static TemplateObject getExcutingTemplateObject() {
        return excutingTemplateObject;
    }

    public static void setExcutingTemplateObject(TemplateObject excutingTemplateObject) {
        ExcuteTracker.excutingTemplateObject = excutingTemplateObject;
    }

    
    
}
