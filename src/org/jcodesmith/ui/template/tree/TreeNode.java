/**
 * 
 */
package org.jcodesmith.ui.template.tree;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jcodesmith.ui.db.tree.BaseTreeNode;

/**
 * @author greki.shen
 *  模板目录树节点
 */
public class TreeNode extends BaseTreeNode{
    
    public TreeNode(String name) {
        super(name);
    }

    private NodeType type;
    /**
     * 是否加载过叶子节点
     */
    private boolean isLoadedChildren=false;
    
    
    public boolean isLoadedChildren() {
        return isLoadedChildren;
    }
    public void setLoadedChildren(boolean isLoadedChildren) {
        this.isLoadedChildren = isLoadedChildren;
    }

    /**
     * 真实路径
     */
    private String fullPathName;
    
    public boolean isDiretory() {
        return NodeType.DIR.equals(type);
    }
    public NodeType getType() {
        return type;
    }
    public void setType(NodeType type) {
        this.type = type;
    }
    public String getFullPathName() {
        return fullPathName;
    }
    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }

    /**
     * 获取节点图片，子类可覆盖
     * @return
     */
    public Image getImage() {
        String imageKey = ISharedImages.IMG_OBJ_FILE;
        if(type.equals(NodeType.DIR)){
            imageKey= ISharedImages.IMG_OBJ_FOLDER;
        }
        return PlatformUI.getWorkbench().getSharedImages()
                .getImage(imageKey);
    }

    public static enum NodeType{
        DIR,FREEMARK,VELOCITY;
        public static NodeType getFromFile(File file){
            if(file.isDirectory()){
                return DIR;
            }else{
                if(file.getName().toLowerCase().endsWith(".vm")){
                    return VELOCITY;
                }else if(file.getName().toLowerCase().endsWith(".ftl")){ {
                    return FREEMARK;
                }
            }
            return null;
            
        }
    }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class arg0) {
        if (IResource.class.equals(arg0))
            return null;
        return null;
    }
}
