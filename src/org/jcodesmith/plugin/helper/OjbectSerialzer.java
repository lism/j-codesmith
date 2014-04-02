package org.jcodesmith.plugin.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

public class OjbectSerialzer implements Serializer {

    @Override
    public String wirteObject(Object obj) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();  
        ObjectOutputStream objectOut;  
        try {  
            objectOut = new ObjectOutputStream(output);  
            objectOut.writeObject(obj);  
            objectOut.close();  
            output.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {
            return output.toString("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            return "";
        } 
    }

    @Override
    public Object readObject(String str) {
        byte[] a;
        try {
            a = str.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }
        ByteArrayInputStream input = new ByteArrayInputStream(a);  
        ObjectInputStream objectIn;  
        Object object = null;  
        try {  
            objectIn = new ObjectInputStream(input);      
            object = objectIn.readObject();  
            objectIn.close();  
            input.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        }  
          
        return object;
    }

}
