package org.jcodesmith.engine;

import java.io.StringWriter;

import org.jcodesmith.engine.factory.TemplateEngineFactory;

public class RenderResolver {
    
    
    public static void render(Object context,String templatePath,StringWriter wr){
        
        
       ITemplateEngine templateEngine= TemplateEngineFactory.getTemplateEngine(templatePath);
        
       
       templateEngine.merge(templatePath, context, wr);
        
    }
}
