package org.jcodesmith.engine.freemarker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import freemarker.cache.TemplateLoader;

/**
 * 加载模板，支持文件路径加载和classpath加载
 * @Type JCodesmithTemplateloader
 * @Desc 
 * @author DELL
 * @date 2013年11月13日
 * @Version V1.0
 */
public class FreeMarkerTemplateloader implements TemplateLoader {
    
    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    
    private Class<?> loaderClass=this.getClass();
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object findTemplateSource(final String fullPath) throws IOException {
        try {
            //从classpath中查找
            if(fullPath.startsWith(CLASSPATH_URL_PREFIX)){
                return loaderClass.getResource(fullPath.substring(CLASSPATH_URL_PREFIX.length()));
            }
            //从文件目录中查找
            return AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws IOException {
                    File source = new File(fullPath);
                    if(!source.isFile()) {
                        return null;
                    }
                    return source;
                }
            });
        }
        catch(PrivilegedActionException e)
        {
            throw (IOException)e.getException();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public long getLastModified(final Object templateSource) {
        //file
        if(templateSource instanceof File){
            return ((Long)(AccessController.doPrivileged(new PrivilegedAction()
            {
                public Object run()
                {
                    return new Long(((File)templateSource).lastModified());
                }
            }))).longValue();
        }
        
        //classpath
        if(templateSource instanceof URL){
            return new File(((URL) templateSource).getFile()).lastModified();
        }
        return 0;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Reader getReader(final Object templateSource, final String encoding) throws IOException {
        //file
        if (templateSource instanceof File) {
            try {
                return (Reader) AccessController.doPrivileged(new PrivilegedExceptionAction() {
                    public Object run() throws IOException {
                        if (!(templateSource instanceof File)) {
                            throw new IllegalArgumentException("templateSource is a: "
                                    + templateSource.getClass().getName());
                        }
                        return new InputStreamReader(new FileInputStream((File) templateSource), encoding);
                    }
                });
            } catch (PrivilegedActionException e) {
                throw (IOException) e.getException();
            }
        }
        //classpath
        if(templateSource instanceof URL){
            URL url=(URL) templateSource;
            return new InputStreamReader( url != null ? url.openStream() : null, encoding);
        }
        return null;
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {
        // Do nothing.
    }

}
