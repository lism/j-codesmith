/*
 * Project: jcodesmith
 * 
 * File Created at 2013年11月25日
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
package org.jcodesmith.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.jcodesmith.JCodeSmithActivator;
import org.jcodesmith.db.config.DatabaseConfig;
import org.jcodesmith.db.config.DatabaseConfigManager;
import org.jcodesmith.plugin.helper.PluginLogger;
import org.jcodesmith.ui.db.views.DataBaseView;
import org.jcodesmith.ui.wizard.DatabaseWizard;

import freemarker.core.ParseException;

/**
 * 
 * @Type CustomVarDialog
 * @Desc 编辑连接
 * @author DELL
 * @date 2014年1月8日
 * @Version V1.0
 */
public class DbConfigListDialog extends Dialog {

    private List conList;

    private Button addBtn;

    private Button editBtn;

    private Button deleteBtn;

    private DatabaseConfig retConfig;

    private String selectedName;
    
    private int shellStyle = SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE;

    protected DbConfigListDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(shellStyle);
    }

    protected DbConfigListDialog(Shell parentShell, String selectedName) {
        super(parentShell);
        this.selectedName = selectedName;
        setShellStyle(shellStyle);
    }

    public static void main(String[] args) throws ParseException {

        final Shell shell = new Shell(SWT.DIALOG_TRIM);
        DbConfigListDialog dg = new DbConfigListDialog(shell);
        dg.open();
    }

    /**
     * 定义对话框界面
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        // 2列
        GridLayout layout = new GridLayout(4, false);
        GridData gdc = new GridData(GridData.FILL_BOTH);
        gdc.widthHint = 450;
        gdc.heightHint = 400;
        container.setLayoutData(gdc);
        container.setLayout(layout);

        // 数据源 group
        Group gp = new Group(container, SWT.SHADOW_IN);
        gp.setLayout(new GridLayout(1, false));
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 4;
        gp.setText("datasource");
        gp.setLayoutData(gd);

        GridData tblgd = new GridData(GridData.FILL_BOTH);
        int style = SWT.BORDER | SWT.SINGLE;
        conList = new List(gp, style);
        conList.setLayoutData(tblgd);

        // 设置值
        refreshList();

        gd = new GridData(GridData.FILL_VERTICAL);
        // 添加
        creatEditButton(container, true);
        // 编辑
        creatEditButton(container, false);
        // 删除
        creatDeleteButton(container);

        applyDialogFont(container);

        // 选择表
        setListSelected();

        return container;
    }

    private void refreshList() {
        conList.setItems(DatabaseConfigManager.getConfigMgr().listAllKey());
    }

    private void creatDeleteButton(Composite parent) {
        deleteBtn = new Button(parent, SWT.PUSH);
        deleteBtn.setText("delete");
        GridData btnGd = new GridData(50, 28);
        btnGd.horizontalAlignment = SWT.RIGHT;
        deleteBtn.setLayoutData(btnGd);
        deleteBtn.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                int idx = conList.getSelectionIndex();
                if (idx == -1) {
                    PluginLogger.openInformation("please select one item to delete.");
                    return;
                }
                DataBaseView.deleteConnection(DatabaseConfigManager.getConfigMgr().get(conList.getItem(idx)));
                refreshList();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void creatEditButton(Composite parent, boolean isAdd) {
        Button btn = new Button(parent, SWT.PUSH);
        boolean isEdit = false;
        if (isAdd) {
            addBtn = btn;
            addBtn.setText("");
            btn.setText("add");
        } else {
            isEdit = true;
            editBtn = btn;
            editBtn.setText("");
            btn.setText("edit");
        }
        GridData btnGd = new GridData(50, 28);
        btnGd.horizontalAlignment = SWT.RIGHT;
        btn.setLayoutData(btnGd);
        btn.addSelectionListener(new EditContionListener(isEdit));
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
        String name = getSelectItem();
        if (name != null) {
            retConfig = DatabaseConfigManager.getConfigMgr().get(name);
        }
        setReturnCode(OK);
        close();
    }

    public DatabaseConfig getRetSelectConfig() {
        return retConfig;
    }

    private String getSelectItem() {
        int idx = conList.getSelectionIndex();
        if (idx != -1) {
            return conList.getItem(idx);
        }
        return null;
    }

    private void setListSelected() {

        int i = 0;
        for (String name : conList.getItems()) {
            if (name.equals(selectedName)) {
                conList.select(i);
            }
            i++;
        }
    }

    /**
     * 编辑连接
     * 
     * @author DELL
     * @version V1.0
     */
    class EditContionListener implements SelectionListener {

        private boolean isEdit = false;

        public EditContionListener(boolean isEdit) {
            this.isEdit = isEdit;
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            DatabaseWizard wizard = null;
            if (isEdit) {
                String name = getSelectItem();
                if (name == null) {
                    PluginLogger.openInformation("please select a item to edit");
                    return;
                }
                DatabaseConfig dbconfig = DatabaseConfigManager.getConfigMgr().get(name);
                wizard = new DatabaseWizard(dbconfig, true);
            } else {
                wizard = new DatabaseWizard(null, false);
            }

            WizardDialog dialog = new WizardDialog(JCodeSmithActivator.getActiveWorkbenchShell(), wizard);
            int ret = dialog.open();
            if (ret == Window.OK) {
                DataBaseView.addConnection(wizard.getDatabaseConfig());
                refreshList();
            }

        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            // TODO Auto-generated method stub

        }
    }

}
