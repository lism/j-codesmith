package org.jcodesmith.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class FileUtil {

    /**
     * 加载资源文件
     * 
     * @param filename
     * @return
     */
    public static Properties loadProperties(String filename) {
        File file = new File(filename);
        if (file.exists() == false) {
            throw new RuntimeException("can't find file[" + filename + "]");
        }
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(file));
            return p;
        } catch (IOException e) {
            String error = "read properties from file[" + filename + "] error.";
            throw new RuntimeException(error, e);
        }
    }

    public static void write(String path, String content) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("path can't be null.");
        }
        OutputStreamWriter osw= null;
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }else{
                String parent=file.getParent();
                if(parent!=null){
                    File pfile=new File(parent);
                    if(!pfile.exists()){
                        pfile.mkdirs();
                    }
                }
            }
            osw = new OutputStreamWriter(new FileOutputStream(path, true),"UTF-8");
            osw.write(content);
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String read(String path) {
        InputStreamReader fr=null;
        try {
            fr =  new InputStreamReader(new FileInputStream(path), "UTF-8");
            StringBuffer sb = new StringBuffer();
            int c;
            while ((c = fr.read()) != -1) {
                sb.append((char) c);
            }
            return sb.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
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
        return null;
    }
    
    public static boolean isRelativePath(String path){
        if(path.startsWith("/")||path.indexOf(":")>0){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 是否是windows操作系统
     * @return
     */
    public static boolean isWindows(){
        Properties prop = System.getProperties();

        String os = prop.getProperty("os.name");
        System.out.println(os);

       if(os.startsWith("win") || os.startsWith("Win") ){
           return true;
       }
       return false;
    }
}
