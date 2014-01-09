package org.jcodesmith.ui.db.tree;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;
import org.jcodesmith.db.dal.DataBaseType;

/**
 * 数据库连接视图树状节点基类
 * 
 * @author greki.shen
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractTreeNode extends BaseTreeNode implements IAdaptable {
	protected AbstractTreeNode parent;

	protected boolean expanded;

	public AbstractTreeNode(String name) {
	    super(name);
	}


	public Object getAdapter(Class key) {
		return null;
	}

	/**
	 * Returns the type.
	 * 
	 * @return int
	 */
	public abstract NodeType getNodeType();

	public DataBaseType getDatabaseType() {
		DatabaseNode connModel = this.getRootDatabaseModel();
		DataBaseType databaseType = connModel.getDatabaseConfig().getDatabaseType();

		return databaseType;
	}

	/**
	 * 返回树中根节点ConnectionModel
	 * 
	 * @param model
	 * @return
	 */
	public DatabaseNode getRootDatabaseModel() {
		BaseTreeNode model = this;
		while (!(model instanceof DatabaseNode) && model != null) {
			model = getParent();
		}
		return (DatabaseNode) model;
	}

	/**
	 * 返回当前节点的图标
	 * 
	 * @return
	 */
	public abstract Image getImage();

	/**
	 * 返回当前节点的名称
	 * 
	 * @return
	 */
	public abstract String getText();


	/**
	 * Converts a database name to a Java variable name.
	 */
	public static String dbNameToVarName(String columnName) {
		if (columnName == null)
			return null;

		StringBuilder fieldName = new StringBuilder(columnName.length());

		boolean toUpper = false;
		for (int i = 0; i < columnName.length(); i++) {
			char ch = columnName.charAt(i);
			if (ch == '_') {
				toUpper = true;
			} else if (toUpper) {
				fieldName.append(Character.toUpperCase(ch));
				toUpper = false;
			} else {
				fieldName.append(Character.toLowerCase(ch));
			}
		}

		return fieldName.toString();
	}

}
