package org.jcodesmith.ui.wizard;


import org.eclipse.jface.wizard.Wizard;
import org.jcodesmith.db.config.DatabaseConfig;
import org.jcodesmith.plugin.resources.PluginMessages;

/**
 * 数据库连接向导
 * 
 * @author greki.shen
 * 
 */
public class DatabaseWizard extends Wizard {
	ConnectionWizardPage databasePage;
	private DatabaseConfig databaseConfig;

	public DatabaseWizard(DatabaseConfig databaseConfig, boolean isEdit) {
		super.setWindowTitle(isEdit ? PluginMessages.DB_CONNECTION_EDIT : PluginMessages.DB_CONNECTION_NEW);
		this.databaseConfig = databaseConfig;
	}

	public DatabaseWizard() {
		super.setWindowTitle(PluginMessages.DB_CONNECTION_NEW);
		this.databaseConfig = new DatabaseConfig();
	}

	public boolean performFinish() {
		 boolean result= databasePage.performFinish();
		 this.databaseConfig=databasePage.getDatabaseConfig();
		 return result;
	}

	@Override
	public void addPages() {
		this.databasePage = new ConnectionWizardPage(PluginMessages.DB_CONNECTION_NEW, this.databaseConfig);
		super.addPage(databasePage);
	}

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }


}
