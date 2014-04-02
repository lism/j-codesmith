package org.jcodesmith.ui.template.views;

import java.util.List;

import org.jcodesmith.engine.TemplateProperty;
import org.jcodesmith.plugin.helper.ConfigFileData;

public class TemplateExcuteData implements ConfigFileData {

    /**
     * 
     */
    private static final long serialVersionUID = 3250167562689691976L;

    private String path;
    
    private List<TemplateProperty> templateProperties;
    
    private long time;
    
    @Override
    public String getkey() {
        return path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<TemplateProperty> getTemplateProperties() {
        return templateProperties;
    }

    public void setTemplateProperties(List<TemplateProperty> templateProperties) {
        this.templateProperties = templateProperties;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    
    
    
}
