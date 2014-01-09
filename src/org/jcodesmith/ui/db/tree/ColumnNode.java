package org.jcodesmith.ui.db.tree;

import static org.jcodesmith.plugin.resources.IconResources.COLUMN_ICON;
import static org.jcodesmith.plugin.resources.IconResources.FK_ICON;
import static org.jcodesmith.plugin.resources.IconResources.PK_ICON;



import org.eclipse.swt.graphics.Image;
import org.jcodesmith.db.meta.ColumnMeta;
import org.jcodesmith.plugin.resources.IconResources;

/**
 * 字段信息
 * 
 * @author greki.shen
 * 
 */
public class ColumnNode extends AbstractTreeNode {
	private ColumnMeta columnMeta;

	public ColumnNode(ColumnMeta columnMeta) {
		super(columnMeta.getName());
		this.columnMeta = columnMeta;
	}

	public ColumnMeta getColumnMeta() {
		return columnMeta;
	}

	public void setColumnMeta(ColumnMeta columnMeta) {
		this.columnMeta = columnMeta;
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.Column;
	}


	@Override
	public Image getImage() {
		if (columnMeta.isPrimaryKey()) {
			return IconResources.getImage(PK_ICON);
		} else if (columnMeta.isForeignKey()) {
			return IconResources.getImage(FK_ICON);
		} else {
			return IconResources.getImage(COLUMN_ICON);
		}
	}

	@Override
	public String getText() {
		return columnMeta.getName();
	}

	
}