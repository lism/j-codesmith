package org.jcodesmith.plugin.helper;

public interface Serializer {
    
    public String wirteObject(Object obj);
    
    public Object readObject(String str);
    
}
