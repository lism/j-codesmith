package org.jcodesmith.plugin.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;

public class PluginConfigFile<T extends ConfigFileData>  {
    private Map<String, T> map = new ConcurrentHashMap<String, T>();
    
    private String fullpath;
    private Class<?>  dataClazz;

    public PluginConfigFile(String path, Class<?> tClazz) {
        this.fullpath = path;
        this.dataClazz=   tClazz;
    }

    /**
     * 序列化成json
     */
    public  void saveToFile() {
        String path = fullpath;
        FileWriter fw = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            fw = new FileWriter(path);

            fw.write(JSONObject.toJSONString(getList()));

            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
        FileReader fr = null;
        try {
            fr = new FileReader(path);
            StringBuffer sb = new StringBuffer();
            int c;
            while ((c = fr.read()) != -1) {
                sb.append((char) c);
            }
            putlist((List<T>) JSONObject.parseArray(sb.toString(), dataClazz));
        } catch (FileNotFoundException e1) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
