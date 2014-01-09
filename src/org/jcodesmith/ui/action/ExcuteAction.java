/**
 * 
 */
package org.jcodesmith.ui.action;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.internal.corext.refactoring.code.TempAssignmentFinder;
import org.eclipse.jdt.internal.ui.preferences.ProjectSelectionDialog;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.jcodesmith.JCodeSmithActivator;
import org.jcodesmith.engine.TemplateObject;
import org.jcodesmith.plugin.helper.PluginLogger;
import org.jcodesmith.plugin.resources.DefalutTemplateFile;
import org.jcodesmith.ui.dialog.ExcuteDialog;

/**
 * 生成默认的DO对象
 * 
 * @author greki.shen
 */
public class ExcuteAction extends Action {

    protected  TemplateObject tplObject;
    
    public ExcuteAction( ) {
    }
    
    public TemplateObject getTplObject() {
        return tplObject;
    }

    public void setTplObject(TemplateObject tplObject) {
        this.tplObject = tplObject;
    }

    public void setTemplatePath(String templatePath) {
        tplObject = new TemplateObject(templatePath);
    }

    public ExcuteAction( String templatePath) {
        tplObject = new TemplateObject(templatePath);
    }

    public ExcuteAction( TemplateObject templateObject) {
        tplObject = templateObject;
    }

    public TemplateObject getTemplateObject(){
        return tplObject;
    }
    
    /**
     * 打开创建连接向导页面
     */
    @Override
    public void run() {
        try {
            if(tplObject==null ){
                PluginLogger.log(IStatus.ERROR,"没有可执行的模板");
                return;
            }
            ExcuteDialog dialog = new ExcuteDialog(JCodeSmithActivator.getActiveWorkbenchShell(), tplObject);
            dialog.open();
        } catch (Exception e) {
            PluginLogger.log(e);
            PluginLogger.openInformation(e.getMessage());
        }

    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
     */
    public void dispose() {
        if (tplObject!=null) {
            tplObject = null;
        }
       
    }

}
