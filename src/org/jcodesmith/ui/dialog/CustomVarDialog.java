package org.jcodesmith.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcodesmith.engine.CustomVariable;
import org.jcodesmith.plugin.helper.PluginLogger;

/**
 * 数据库链接参数设置页面
 * 
 * @author greki.shen
 * 
 */
public class CustomVarDialog extends Dialog {
    private Text name;
    private Text className;
    private Text desc;
    private CustomVariable var = null;
    private int shellStyle = SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE;

    public CustomVarDialog(Shell parentShell, CustomVariable var) {
        super(parentShell);
        if(var!=null){
            this.var = var;
        }else{
            this.var =new CustomVariable();
        }
    }

    @Override
    public Control createDialogArea(Composite parent) {

        // 定义布局
        Composite container = new Composite(parent, shellStyle);
        GridLayout layout = new GridLayout(2, false);
        GridData gdc = new GridData(GridData.FILL_BOTH);
        gdc.widthHint = 450;
        gdc.heightHint =150;
        container.setLayoutData(gdc);
        container.setLayout(layout);
        layout.verticalSpacing = 9;

        // 名称
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        this.name = this.createText(container, "*Name", gd, SWT.BORDER | SWT.SINGLE);
        name.setToolTipText("the name which is Used as tempalte variable");
        this.name.setText(this.var.getName());

        // 类名
        this.className = this.createText(container, "*Class Name", gd, SWT.BORDER | SWT.SINGLE);
        this.className.setText(this.var.getClassName());
        this.className.setToolTipText("Class name which is used to create  instance for a var ");

        // 自定义驱动包
        this.desc = this.createText(container, "*Description", gd, SWT.BORDER | SWT.SINGLE );
        this.desc.setText(this.var.getDesc());
        this.desc.setToolTipText("description");
        
        return container;
    }

    /**
     * 创建一行编辑框
     * 
     * @param parent
     * @param capital
     * @param gd
     * @param style the style of text control to construct
     * @return
     */
    private Text createText(Composite parent, String capital, GridData gd, int style) {
        Label label = new Label(parent, SWT.NULL);
        label.setText(capital);
        Text text = new Text(parent, style);
        text.setLayoutData(gd);
        return text;
    }
    // 重载这个方法
    @Override
    protected Control createButtonBar(Composite parent) {
        Control btnBar = super.createButtonBar(parent);
        getButton(IDialogConstants.CANCEL_ID).setVisible(true);
        Button ok = getButton(IDialogConstants.OK_ID);
        ok.setText("OK");
        return btnBar;
    }
    
    @Override
    protected void okPressed() {
      
        var.setName(name.getText());
        var.setDesc(desc.getText());
        var.setClassName(className.getText());
        if(var.getName().isEmpty() || var.getName().isEmpty()){
            PluginLogger.openInformation("Name and ClassName must be input.");
            return;
        }
        setReturnCode(OK);
        close();
    }
    
    public CustomVariable getVariable(){
        return var;
    }

}
