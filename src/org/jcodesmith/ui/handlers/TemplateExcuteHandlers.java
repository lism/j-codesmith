package org.jcodesmith.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcodesmith.engine.factory.TemplateEngineFactory;
import org.jcodesmith.plugin.resources.PluginMessages;
import org.jcodesmith.ui.action.ExcuteAction;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class TemplateExcuteHandlers extends AbstractHandler {
    /**
     * The constructor.
     */
    public TemplateExcuteHandlers() {
    }

    /**
     * the command has been executed, so extract extract the needed information
     * from the application context.
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {

         IWorkbenchWindow window =
         HandlerUtil.getActiveWorkbenchWindowChecked(event);
         
         boolean isopen=false;
        try {
            ISelection selection = HandlerUtil.getActiveMenuSelection(event);

            if (selection.isEmpty() || !(selection instanceof IStructuredSelection))
                return null;
            IStructuredSelection ss = (IStructuredSelection) selection;
            if (ss.size() != 1)
                return null;

            Object o = ss.getFirstElement();
            if (!(o instanceof IAdaptable))
                return null;

            IAdaptable element = (IAdaptable) o;
            Object resource = element.getAdapter(IResource.class);
            if (!(resource instanceof IFile))
                return null;

            IFile file = (IFile) resource;

            String path = file.getRawLocation().toOSString();
            if (!TemplateEngineFactory.isValidExtension(path)) {
                return null;
            }

            ExcuteAction excute = new ExcuteAction();
            excute.setTemplatePath(path);
            excute.run();
            isopen=true;
            return null;
        } catch (Exception e) {
             MessageDialog.openInformation(window.getShell(), "JCodeSmith",
            e.getMessage());
            return null;
        }finally{
            if(!isopen){
                MessageDialog.openInformation(window.getShell(), "JCodeSmith",
                       PluginMessages.TP_EXCUTE_ERROR);
            }
        }

    }
}
