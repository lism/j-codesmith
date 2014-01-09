package org.jcodesmith.ui.template.views;

import org.jcodesmith.plugin.helper.ConfigFileData;

public class TemplateProject implements ConfigFileData {

    
    private String path;
    
    private String name;
    
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    
}
