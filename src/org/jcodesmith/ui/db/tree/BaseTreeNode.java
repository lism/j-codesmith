package org.jcodesmith.ui.db.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * 树状节点基类
 * 
 * @author greki.shen
 * 
 */
@SuppressWarnings("rawtypes")
public  class BaseTreeNode implements IAdaptable {
	protected String name;
	protected List<BaseTreeNode> children = new ArrayList<BaseTreeNode>();
	protected BaseTreeNode parent;

	protected boolean expanded;

	public BaseTreeNode(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}

	public BaseTreeNode getParent() {
		return parent;
	}

	public Object getAdapter(Class key) {
		return null;
	}

	public final void addChild(BaseTreeNode child) {
	    
	    if(child==null){
            return;
        }
		children.add(child);
		child.parent = this;
	}

	public final void removeChild(BaseTreeNode child) {
		children.remove(child);
		child.parent = null;
	}
	
	/**
	 * 获取节点图片，子类可覆盖
	 * @return
	 */
    public Image getImage() {
        String imageKey = ISharedImages.IMG_OBJ_FILE;
        return PlatformUI.getWorkbench().getSharedImages()
                .getImage(imageKey);
    }
    
	/**
	 * 清除子节点
	 */
	public void clear() {
		if (children != null) {
			children.clear();// children = new ArrayList<Model>();
		}
	}

	public final BaseTreeNode[] getChildren() {
		if (children == null) {
			return new BaseTreeNode[0];
		} else {
			return (BaseTreeNode[]) children.toArray(new BaseTreeNode[children.size()]);
		}
	}

	public BaseTreeNode getChildByName(String name) {
		BaseTreeNode[] children = getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i].getName().equalsIgnoreCase(name)) {
				return children[i];
			}
		}
		return null;
	}

	public List<BaseTreeNode> getChildrenList() {
		return children;
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

}
