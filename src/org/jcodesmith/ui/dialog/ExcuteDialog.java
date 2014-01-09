package org.jcodesmith.ui.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.jcodesmith.db.meta.TableMeta;
import org.jcodesmith.engine.SupportType;
import org.jcodesmith.engine.TemplateObject;
import org.jcodesmith.engine.TemplateProperty;
import org.jcodesmith.plugin.helper.PluginLogger;

public class ExcuteDialog extends Dialog {

    Button save;
    Button saveAs;
    Button copy;

    Text result;
    private int shellStyle = SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE;

    private Map<String, Control> controlMap = new HashMap<String, Control>();
    private TemplateObject tplObject;

    public ExcuteDialog(Shell parentShell, String path) {
        super(parentShell);
        this.tplObject = new TemplateObject(path);
        setShellStyle(shellStyle);
        setBlockOnOpen(false);
    }

    public ExcuteDialog(Shell parentShell, TemplateObject tplObject) {
        super(parentShell);
        this.tplObject = tplObject;
        setShellStyle(shellStyle);
        setBlockOnOpen(false);
    }

    private void initLeftPropertys(Composite parent) {
        for (TemplateProperty p : tplObject.getPropertyList()) {
            initProperty(parent, p);
        }
    }

    private void initProperty(Composite parent, TemplateProperty property) {

        Label label = new Label(parent, SWT.NULL);
        label.setText(property.getName());
        // 横向满
        GridData leftgd = new GridData(GridData.FILL_HORIZONTAL);
        if (SupportType.TABLE.getType().equals(property.getType())) {
            leftgd.horizontalSpan = 1;
        } else {
            leftgd.horizontalSpan = 2;
        }
        Control ctr = null;
        if (SupportType.BOOLEAN.getType().equals(property.getType())) {
            Combo combo = new Combo(parent, SWT.NULL);
            combo.setItems(new String[] { "true", "false" });

            if ("false".equals(property.getDefaultValue())) {
                combo.select(1);
            } else {
                combo.select(0);
            }
            ctr = combo;
        } else {
            Text text = new Text(parent, SWT.BORDER | SWT.SINGLE);
            text.setText(property.getDefaultValue());
            ctr = text;
            if (SupportType.TABLE.getType().equals(property.getType())
                    || SupportType.TABLES.getType().equals(property.getType())) {
                // creat select type button
                Button b = new Button(parent, SWT.PUSH);
                b.setText("Select");
                GridData btnGd = new GridData(50, 30);
                btnGd.verticalAlignment = SWT.END;
                b.setLayoutData(btnGd);
                ctr.setData(property.getValue());
                boolean isMulti = false;
                if (SupportType.TABLES.getType().equals(property.getType())) {
                    isMulti = true;
                }
                b.addSelectionListener(new SelectTableListener(ctr, isMulti));
                text.setText("");
            }
        }

        ctr.setToolTipText(property.getDescription());
        ctr.setLayoutData(leftgd);
        ctr.setData(property.getValue());
        // 保持property和控件的对关系
        controlMap.put(property.getName(), ctr);
    }

    /**
     * 定义对话框界面
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        // 2列
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        container.setLayout(layout);

        // 左边
        Composite leftContainer = new Composite(container, SWT.SCROLL_PAGE);
        GridData leftgd = new GridData(GridData.FILL_BOTH);
        leftgd.widthHint = 300;
        leftgd.heightHint = 300;
        leftContainer.setLayoutData(leftgd);

        GridLayout leftLayout = new GridLayout(3, false);
        leftContainer.setLayout(leftLayout);
        // 初始化左边动态控件
        initLeftPropertys(leftContainer);

        // 右边
        Composite rightContainer = new Composite(container, SWT.SCROLL_PAGE);

        GridData rightGd = new GridData(GridData.FILL_BOTH);
        rightGd.widthHint = 700;
        rightGd.heightHint = 700;
        rightContainer.setLayoutData(rightGd);

        GridLayout rightLayout = new GridLayout(3, false);
        rightContainer.setLayout(rightLayout);

        createButtons(rightContainer);

        result = new Text(rightContainer, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData area = new GridData(GridData.FILL_BOTH);
        area.horizontalSpan = 3;
        area.heightHint = 300;
        result.setLayoutData(area);

        GridData gd = new GridData(GridData.FILL_VERTICAL);
        gd.widthHint = 300;

        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        applyDialogFont(container);
        // Group group = new Group(composite,SWT.NONE);
        // ……添加自己的组件
        return container;
    }

    /**
     * 创建测试连接按钮
     * 
     * @param parent
     */
    private void createButtons(Composite parent) {

        save = new Button(parent, SWT.PUSH);
        save.setToolTipText("save");
        save.setImage(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT)
                .createImage());

        GridData btnGd = new GridData(30, 30);
        btnGd.horizontalAlignment = SWT.LEFT;
        save.setLayoutData(btnGd);

        
        
        
        saveAs = new Button(parent, SWT.PUSH);
        saveAs.setToolTipText("save as");
        saveAs.setImage(PlatformUI.getWorkbench().getSharedImages()
                .getImageDescriptor(ISharedImages.IMG_ETOOL_SAVEAS_EDIT).createImage());

        saveAs.setLayoutData(btnGd);
        saveAs.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
                        IHandlerService.class);
                try {
                    handlerService.executeCommand("org.eclipse.ui.file.saveAs", null);
                } catch (Exception ex) {
                    throw new RuntimeException("org.eclipse.ui.file.saveAs not found", ex);
                }

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        copy = new Button(parent, SWT.PUSH);
        copy.setToolTipText("copy");
        copy.setImage(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY)
                .createImage());

        btnGd = new GridData(30, 30);
        btnGd.horizontalAlignment = SWT.LEFT;
        copy.setLayoutData(btnGd);

        copy.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                if (result.getSelectionText() == null || result.getSelectionText().isEmpty()) {
                    result.selectAll();
                }
                result.copy();
                PluginLogger.openInformation("result Has been copied to the clipboard");

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }

        });

    }

    // 重载这个方法
    @Override
    protected Control createButtonBar(Composite parent) {
        Control btnBar = super.createButtonBar(parent);
        getButton(IDialogConstants.CANCEL_ID).setVisible(true);
        Button ok = getButton(IDialogConstants.OK_ID);
        ok.setText("Excute");

        Button cancel = getButton(IDialogConstants.CANCEL_ID);
        cancel.setText("Close");
        return btnBar;
    }

    @Override
    protected void okPressed() {

        // 页面的值设置到属性对象
        setToPropertys();

        setReturnCode(OK);

        String out = tplObject.merge();
        result.setText(out);
        // close();
    }

    /**
     * 页面的值设置到属性对象
     */
    private void setToPropertys() {

        for (TemplateProperty p : tplObject.getPropertyList()) {
            Control ctr = controlMap.get(p.getName());
            if (SupportType.TABLE.getType().equals(p.getType()) || SupportType.TABLES.getType().equals(p.getType())) {
                p.setValue(ctr.getData());
            } else if (SupportType.BOOLEAN.getType().equals(p.getType())) {
                p.setValue(Boolean.valueOf(((Combo) ctr).getText()));
            } else if (SupportType.DOUBLE.getType().equals(p.getType())) {
                p.setValue(Double.valueOf(((Text) ctr).getText()));
            } else if (SupportType.LONG.getType().equals(p.getType())) {
                p.setValue(Long.valueOf(((Text) ctr).getText()));
            } else if (SupportType.STRING.getType().equals(p.getType())) {
                p.setValue(((Text) ctr).getText());
            }
        }
    }

    /**
     * select table
     * 
     * 
     */
    class SelectTableListener implements SelectionListener {

        private Control ctrl;
        private boolean isMulti;

        public SelectTableListener(Control ctr, boolean isMulti) {
            ctrl = ctr;
            this.isMulti = isMulti;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void widgetSelected(SelectionEvent e) {

            TableMeta tblm = null;
            List<TableMeta> tblmlist = null;
            String dbName = null;
            if (ctrl.getData() != null) {
                if (ctrl.getData() instanceof TableMeta) {
                    tblm = (TableMeta) ctrl.getData();
                } else if (ctrl.getData() instanceof ArrayList) {
                    tblmlist = (List<TableMeta>) ctrl.getData();
                }
            }
            TableSelectDialog d = null;
            if (tblm != null) {
                dbName = tblm.getDatabase().getManulName();
                d = new TableSelectDialog(getParentShell(), tblm.getName(), dbName);
            } else if (tblmlist != null) {
                dbName = tblmlist.get(0).getDatabase().getManulName();
                List<String> list = new ArrayList<String>();
                for (TableMeta m : tblmlist) {
                    list.add(m.getName());
                }
                d = new TableSelectDialog(getParentShell(), list, dbName);
            } else {
                d = new TableSelectDialog(getParentShell(), isMulti);
            }

            int ret = d.open();
            if (ret == OK) {
                ctrl.setData(d.getSelectTable());
                if (ctrl instanceof Text && d.getSelectTable() != null) {
                    ((Text) ctrl).setText(d.getSelectTable().getName());
                }
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {

        }

    }
}
