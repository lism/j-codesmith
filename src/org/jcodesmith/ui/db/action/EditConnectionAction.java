/**
 * 
 */
package org.jcodesmith.ui.db.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jcodesmith.JCodeSmithActivator;
import org.jcodesmith.db.config.DatabaseConfig;
import org.jcodesmith.ui.db.views.DataBaseView;
import org.jcodesmith.ui.wizard.DatabaseWizard;

/**
 * @author greki.shen
 * 数据库连接(添加或者编辑）
 */
public class EditConnectionAction extends Action {
	
	private DatabaseConfig databaseConfig;
	
	public EditConnectionAction (boolean isEdit){
	    if(isEdit){
	        setText("edit");
	        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
	                .getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));
	    }else{
	        setText("add");
	        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
	                .getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
	        
	    }
	}
	
	
	public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public void setDatabaseConfig(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    /**
	 * 打开创建连接向导页面
	 */
	@Override
	public void run() {
		DatabaseWizard wizard = new DatabaseWizard(databaseConfig,databaseConfig!=null);
		WizardDialog dialog = new WizardDialog(JCodeSmithActivator.getActiveWorkbenchShell(), wizard);
		int ret=dialog.open();
		if(ret==Window.OK){
		    DataBaseView.addConnection(wizard.getDatabaseConfig());
		}
	}
	
}
