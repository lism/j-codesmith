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

import java.sql.Connection;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.jcodesmith.db.config.DatabaseConfig;
import org.jcodesmith.db.config.DatabaseConfigManager;
import org.jcodesmith.db.dal.ConnectionManager;
import org.jcodesmith.db.dal.MetaManager;
import org.jcodesmith.db.meta.TableMeta;

import freemarker.core.ParseException;

/**
 * @Type TableSelectDialog
 * @Desc 选择表的对话框
 * @author greki.shen
 * @date 2013年11月25日
 * @Version V1.0
 */
public class TableSelectDialog extends Dialog {

    private List tableList;

    private  java.util.List<TableMeta> tblMetaList;
    
    private Combo datasourceCombo;
    
    private Button addDb;
    
    private boolean isMultiple=false;
    
    private String tableName;
    
    private java.util.List<String> tblNameList;
    
    private String dbName;
    
    private java.util.List<TableMeta> retSelectList=new ArrayList<TableMeta>();
    

    protected TableSelectDialog(Shell parentShell) {
        super(parentShell);
    }
    
    protected TableSelectDialog(Shell parentShell,boolean isMultiple) {
        super(parentShell);
        this.isMultiple=isMultiple;
    }
    
    
    public TableSelectDialog(Shell parentShell,String tableName,String dbName) {
        super(parentShell);
        this.isMultiple=false;
        this.tableName=tableName;
        this.dbName=dbName;
    }
    public TableSelectDialog(Shell parentShell,java.util.List<String> tblNameList,String dbName) {
        super(parentShell);
        this.isMultiple=true;
        this.tblNameList=tblNameList;
        this.dbName=dbName;
    }
    

    public static void main(String[] args) throws ParseException {

        final Shell shell = new Shell(SWT.DIALOG_TRIM);
        TableSelectDialog dg = new TableSelectDialog(shell);
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
        gdc.widthHint = 550;
        gdc.heightHint = 600;
        container.setLayoutData(gdc);
        container.setLayout(layout);

        // 数据源
        Label label = new Label(container, SWT.NULL);
        label.setText("Data Source");
        datasourceCombo = new Combo(container, SWT.NULL);
        GridData gddatasource = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gddatasource.widthHint = 200;
        datasourceCombo.setLayoutData(gddatasource);
        datasourceCombo.setItems(DatabaseConfigManager.getConfigMgr().listAllKey());
        //监听
        datasourceCombo.addSelectionListener(new DatasourceChangeListener());

        createNewDbButton(container);
        
        // tables group
        Group gp = new Group(container, SWT.SHADOW_IN);
        gp.setLayout(new GridLayout(1, false));
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        gp.setText("tables");
        gp.setLayoutData(gd);

        GridData tblgd = new GridData(GridData.FILL_BOTH);
        int style=SWT.BORDER | SWT.SINGLE|SWT.V_SCROLL;
        if(isMultiple){
            style=SWT.BORDER | SWT.MULTI|SWT.V_SCROLL;
        }
        tableList = new List(gp, style);
        tableList.setLayoutData(tblgd);

        
       
        
        gd = new GridData(GridData.FILL_VERTICAL);

        applyDialogFont(container);
        
        //选中数据源
        selectDBCombo(dbName);
        //选择表
        selectTableList();
        
        
        return container;
    }

    
    /**
     * 创建测试连接按钮
     * 
     * @param parent
     */
    private void createNewDbButton(Composite parent) {

        addDb = new Button(parent, SWT.PUSH);
        addDb.setText("Add DataSource");
        GridData btnGd = new GridData(120, 28);
        btnGd.horizontalAlignment = SWT.RIGHT;
        addDb.setLayoutData(btnGd);

        addDb.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                DbConfigListDialog dg = new DbConfigListDialog(getParentShell());
                int ret=  dg.open();
                if(ret==Window.OK){
                    datasourceCombo.setItems(DatabaseConfigManager.getConfigMgr().listAllKey());
                    if(dg.getRetSelectConfig()!=null){
                        selectDBCombo(dg.getRetSelectConfig().getManulName());
                    }
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });
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
        setRetSelectTables();
        setReturnCode(OK);
         close();
    }
    
    
    private void selectDBCombo(String dbName){
        int i=0;
        for (String name : datasourceCombo.getItems()) {
            if(name.equals(dbName)){
                datasourceCombo.select(i);
                displayTableList();
                break;
            }
            i++;
        }
        
    }
    
    private void selectTableList(){
        if(tblNameList==null && tableName==null ){
            return;
        }
        if(tblNameList==null){
            tblNameList=new ArrayList<String>();
            tblNameList.add(tableName);
        }
        
        for (String tbl : tblNameList) {
            int i=0;
            for (String name : tableList.getItems()) {
                if(name.equals(tbl)){
                    tableList.select(i);
                }
                i++;
            }
        }
    }
    
    /**
     * 数据源变更监听
     * 
     * @author DELL
     * 
     */
    class DatasourceChangeListener implements SelectionListener {

        @Override
        public void widgetSelected(SelectionEvent e) {
            displayTableList();
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {

        }

    }
    
    public java.util.List<TableMeta>getSelectTables(){
        return retSelectList;
    }
    /**
     * 获取选择中的表格列表
     * @return
     */
    private java.util.List<TableMeta>setRetSelectTables(){
        Connection conn=getConnection();
        for (int i  :  tableList.getSelectionIndices()) {
            TableMeta tbl=tblMetaList.get(i);
            if (tbl.getColumns() == null) {
                tbl.setColumns(MetaManager.getColumns(conn, tbl.getName()));
            }
            retSelectList.add(tbl);
        }
        return retSelectList;
        
    }
    public TableMeta getSelectTable(){
        if(retSelectList!=null && retSelectList.size()>0){
            return retSelectList.get(0);
        }
        return null;
    }

    private void displayTableList() {
        tableList.removeAll();
        Connection conn = getConnection();
        //不初始化列的值
        tblMetaList = MetaManager.getTables(conn,false);
        String[] itmes = new String[tblMetaList.size()];
        int i = 0;
        for (TableMeta tableMeta : tblMetaList) {
            itmes[i] = tableMeta.getName();
            i++;
        }
        tableList.setItems(itmes);
        tableList.setData(tblMetaList);
    }

    private Connection getConnection() {
        String db = datasourceCombo.getText().trim();
        DatabaseConfig config = DatabaseConfigManager.getConfigMgr().get(db);
        Connection conn = ConnectionManager.getConnection(config, false);
        return conn;
    }
}
