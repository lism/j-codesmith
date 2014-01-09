package org.jcodesmith.ui.db.tree;

import static org.jcodesmith.plugin.resources.IconResources.CONNECTED_ICON;
import static org.jcodesmith.plugin.resources.IconResources.DISCONNECTED_ICON;
import static org.jcodesmith.plugin.resources.IconResources.SELECTED_CONNECTED_ICON;




import org.eclipse.swt.graphics.Image;
import org.jcodesmith.db.config.DatabaseConfig;
import org.jcodesmith.plugin.resources.IconResources;

/**
 * 数据库连接基本信息
 * 
 * @author greki.shen
 * 
 */
public class DatabaseNode extends AbstractTreeNode {
	
	/**
	 * 是否激活
	 */
	private boolean isActived;
	
	
	
	public boolean isActived() {
		return isActived;
	}

	public void setActived(boolean isActived) {
		this.isActived = isActived;
	}


	public DatabaseNode( DatabaseConfig dataconfig) {
		super(dataconfig.getManulName());
		this.databaseConfig = dataconfig;
	}

	private DatabaseConfig databaseConfig;

	@Override
	public String getName() {
		return name;
	}


	public DatabaseConfig getDatabaseConfig() {
		return databaseConfig;
	}


	@Override
	public NodeType getNodeType() {
		return NodeType.Database;
	}


	@Override
	public Image getImage() {
		if ( isActived) {
			return IconResources.getImage(SELECTED_CONNECTED_ICON);
		}

		if (this.databaseConfig.isConnected()) {
			return IconResources.getImage(CONNECTED_ICON);
		} else {
			return IconResources.getImage(DISCONNECTED_ICON);
		}
	}

	@Override
	public String getText() {
		return this.name;
	}



}
