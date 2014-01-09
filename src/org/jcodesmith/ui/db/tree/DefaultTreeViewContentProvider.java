/*
 * Project: jcodesmith
 * 
 * File Created at 2013年11月8日
 * 
 * Copyright 2012 Greenline.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Greenline Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Greenline.com.
 */
package org.jcodesmith.ui.db.tree;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.part.ViewPart;

/**
 * @Type DefaultTreeViewContentProvider
 * @Desc 默认的TreeViewContentProvider
 * @author DELL
 * @date 2013年11月8日
 * @Version V1.0
 */
public class DefaultTreeViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

    private BaseTreeNode invisibleRoot;
    private ViewPart viewPart;

    @Override
    public void dispose() {
        
    }
    
    public BaseTreeNode getDataInputRoot(){
        return invisibleRoot;
    }
    
    public DefaultTreeViewContentProvider( ViewPart viewPart) {
        this.invisibleRoot =new  BaseTreeNode("root");
        this.viewPart = viewPart;
    }

    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {

    }

    /**
     * getChildren表示在每次展开树的节点时，如果得到下一级节点的值，当然，树的节点展开了一次就不会再调用这个方法了，
     * 因为在树节点对应的控件TreeItem中，已经创建好了子节点控件了
     */
    @Override
    public Object[] getChildren(Object parent) {
        if (parent instanceof BaseTreeNode) {
            return ((BaseTreeNode) parent).getChildren();
        }
        return new Object[0];
    }

    @Override
    public Object getParent(Object child) {
        if (child instanceof BaseTreeNode) {
            return ((BaseTreeNode) child).getParent();
        }
        return null;
    }

    /**
     * hasChildren就是判断当前节点是否有子节点，有的话，就显示+号
     */
    @Override
    public boolean hasChildren(Object parent) {
        if (parent instanceof BaseTreeNode) {
            BaseTreeNode p = (BaseTreeNode) parent;
            return p.hasChildren();
        }
        return false;
    }

    /**
     * getElements 表示在setInput(Object)的时候，如何从Object中得到一个数组，而使用这个数组去将树的第一层结点显示出来
     */
    @Override
    public Object[] getElements(Object parent) {

        if (parent.equals(viewPart.getViewSite())) {
            if (invisibleRoot != null) {
                return getChildren(invisibleRoot);
            }
        }
        return getChildren(parent);
    }

}
