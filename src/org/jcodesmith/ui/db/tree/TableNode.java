package org.jcodesmith.ui.db.tree;

import static org.jcodesmith.plugin.resources.IconResources.TABLE_ICON;

import org.eclipse.swt.graphics.Image;
import org.jcodesmith.db.meta.TableMeta;
import org.jcodesmith.plugin.resources.IconResources;

/**
 * 表结构model
 * 
 * @author greki.shen
 * 
 */
public class TableNode extends AbstractTreeNode {
	private TableMeta tableMeta;

	// private List<ColumnNode> primaryKeys;

	public TableNode(TableMeta tableMeta) {
		super(tableMeta.getName());
		this.tableMeta = tableMeta;
	}


	public TableMeta getTableMeta() {
		return tableMeta;
	}


	public void setExpanded(boolean b) {
		this.expanded = b;
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.Table;
	}


	@Override
	public Image getImage() {
		return IconResources.getImage(TABLE_ICON);
	}

	@Override
	public String getText() {
		return this.name;
	}

}