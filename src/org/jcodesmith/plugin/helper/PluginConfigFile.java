package org.jcodesmith.plugin.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jcodesmith.utils.FileUtil;

public class PluginConfigFile<T extends ConfigFileData>  {
    private Map<String, T> map = new ConcurrentHashMap<String, T>();
    
    private String fullpath;
    private Class<?>  dataClazz;

    private Serializer serializer=null;
    
    public PluginConfigFile(String path, Class<?> tClazz) {
        this.fullpath = path;
        this.dataClazz=   tClazz;
        this.serializer=new JsonSerializer(dataClazz);
    }
    public PluginConfigFile(String path, Class<?> tClazz,Serializer serializer) {
        this.fullpath = path;
        this.dataClazz=   tClazz;
        this.serializer=serializer;
    }
    /**
     * 序列化成json
     */
    public  void saveToFile() {
        FileUtil.write(fullpath, String.valueOf(serializer.wirteObject(getList())));
    }

    private  List<T> getList() {
        List<T> list = new ArrayList<T>();
        for (T t : map.values()) {
            list.add(t);
        }
        return list;
    }

    private  void putlist(List<T> list) {
        for (T t : list) {
            map.put(t.getkey(), t);
        }
    }

    @SuppressWarnings("unchecked")
    public  void load() {
        String path = PluginHelper.getConfigFilePath(fullpath);
        fullpath = path;
        String json=   FileUtil.read(fullpath);
        try {
            putlist((List<T>) serializer.readObject(json));
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public  void put(T t) {
        map.put(t.getkey(),t);
    }

    public  void remove(T t) {
        map.remove(t.getkey());
    }

    public  T get(String key) {
        return map.get(key);
    }

    public  String[] listAllKey() {
        String[] keys = new String[map.size()];
        int i = 0;
        for (String key : map.keySet()) {
            keys[i] = key;
            i++;
        }
        return keys;
    }

}
