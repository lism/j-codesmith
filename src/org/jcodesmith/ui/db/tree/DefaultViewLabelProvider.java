package org.jcodesmith.ui.db.tree;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class DefaultViewLabelProvider extends LabelProvider {

    public String getText(Object obj) {
        if (obj instanceof BaseTreeNode) {
            return ((BaseTreeNode) obj).getName();
        }
        return obj.toString();
    }

    public Image getImage(Object obj) {
        if (obj instanceof BaseTreeNode) {
            return ((BaseTreeNode) obj).getImage();
        }
        return PlatformUI.getWorkbench().getSharedImages()
                .getImage(ISharedImages.IMG_OBJ_ELEMENT);
    }
}