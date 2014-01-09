/**
 * 
 */
package org.jcodesmith.ui.db.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jcodesmith.db.config.DatabaseConfig;
import org.jcodesmith.plugin.resources.IconResources;
import org.jcodesmith.ui.db.views.DataBaseView;

/**
 * 打开数据库连接
 * @author greki.shen
 */
public class ConnectionAction extends Action {
	
	private DatabaseConfig databaseConfig;
	
	public ConnectionAction (){
	    setText("connect");
        ImageDescriptor imd=ImageDescriptor.createFromImage(IconResources.getImage(IconResources.CONNECTED_ICON));
        setImageDescriptor(imd);
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
    	DataBaseView.connectAndListTable();
	}
	
}
