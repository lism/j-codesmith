package org.jcodesmith.engine;

public class CustomVariable {
    protected String name = "";
    protected String className = "";
    protected String desc = "";
    protected boolean isSystem=false;
    public CustomVariable(){
    }
    
    public CustomVariable(String name, String className, String desc) {
        this.name = name;
        this.className = className;
        this.desc = desc;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
