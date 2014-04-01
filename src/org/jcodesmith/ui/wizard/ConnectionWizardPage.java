package org.jcodesmith.ui.wizard;

import java.sql.Connection;
import java.sql.SQLException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcodesmith.JCodeSmithActivator;
import org.jcodesmith.db.config.DatabaseConfig;
import org.jcodesmith.db.config.DatabaseConfigManager;
import org.jcodesmith.db.config.DbConfigHelper;
import org.jcodesmith.db.dal.ConnectionManager;
import org.jcodesmith.db.dal.JdbcHelper;
import org.jcodesmith.plugin.resources.PluginMessages;

/**
 * 数据库链接参数设置页面
 * 
 * @author greki.shen
 * 
 */
public class ConnectionWizardPage extends WizardPage {
    private Text name;
    private Combo driver;
    private Text url;
    private Text driverJar;
    private Button testButton;
    private boolean isEdit=false;
    private DatabaseConfig databaseConfig = null;

    private FileDialog jarDialog;
    

    /**
     * 数据库连接设置引导页面
     * 
     * @param pageName
     * @param dataconfig ：如果已经
     */
    public ConnectionWizardPage(String pageName, DatabaseConfig dataconfig) {
        super(pageName);
        if(dataconfig!=null){
            isEdit=true;
            this.databaseConfig = dataconfig;
        }else{
            this.databaseConfig =new DatabaseConfig();
        }
    }

    private void initDialog() {
        jarDialog = new FileDialog(getContainer().getShell(), SWT.OPEN | SWT.MULTI);
        jarDialog.setFilterExtensions(new String[] { "*.jar", "*.zip", "*.*" });
        jarDialog.setFilterNames(new String[] { "Jar Files (*.jar)", "Zip Files (*.zip)", "All Files (*.*)" });
    }

    @Override
    public void createControl(Composite parent) {
        // 初始化打开jar对话框
        this.initDialog();

        // 定义布局
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout(3, false);
        GridData gdc = new GridData(GridData.FILL_BOTH);
        gdc.widthHint = 450;
        gdc.heightHint = 400;
        container.setLayoutData(gdc);
        container.setLayout(layout);
        layout.verticalSpacing = 9;

        // 配置名称
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;

        this.name = this.createText(container, "*Name", gd, SWT.BORDER | SWT.SINGLE);
        name.setToolTipText(PluginMessages.DB_CONFIG_NAME);
        this.name.setText(this.databaseConfig.getManulName());
        if(this.name.getText()!=null && !this.name.getText().isEmpty()){
            this.name.setEditable(false);
        }
        // 驱动类型
        this.driver = this.createDriverTypeCombo(container, "*Driver", gd);
        this.driver.setToolTipText(PluginMessages.DB_CONFIG_DRIVER);
        this.driver.setText(this.databaseConfig.getDriverClazz());

        // 连接串
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        gd.heightHint = 60;
        this.url = this.createText(container, "*JDBC URL", gd, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        this.url.setText(this.databaseConfig.getUrl());
        this.url.setToolTipText(PluginMessages.DB_CONFIG_JDBC_URL);

        // 自定义驱动包
        this.driverJar = this.createJars(container);
        this.driverJar.setText(this.databaseConfig.getDriverJars());
        this.url.setToolTipText(PluginMessages.DB_CONFIG_JAR);
        // 测试按钮
        createTestButton(container);

        this.setControl(container);
        this.setPageComplete(true);
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

    /**
     * 创建测试连接按钮
     * 
     * @param parent
     */
    private void createTestButton(Composite parent) {

        testButton = new Button(parent, SWT.PUSH);
        testButton.setText("Test");
        GridData btnGd = new GridData(100, 40);
        btnGd.horizontalSpan = 3;
        btnGd.horizontalAlignment = SWT.RIGHT;
        testButton.setLayoutData(btnGd);

        testButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                setDatabaseConfig();
                Connection conn = ConnectionManager.getConnection(databaseConfig, true);

                String message = "connect failed";
                try {
                    JdbcHelper.testConnection(conn);
                    message = "connect successful!";
                } catch (SQLException e1) {
                    message += e1.getMessage();
                }

                MessageDialog.openInformation(JCodeSmithActivator.getActiveWorkbenchShell(), "connect test", message);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 创建依赖jar包text
     * 
     * @param parent
     * @return
     */
    private Text createJars(Composite parent) {
        Label label = new Label(parent, SWT.NULL);
        label.setText("*Driver JARs");
        Text text = new Text(parent, SWT.BORDER | SWT.MULTI);
        GridData area = new GridData(GridData.FILL_BOTH);
        area.heightHint = 65;
        text.setLayoutData(area);

        Button button = new Button(parent, SWT.PUSH);
        button.setText("select");
        GridData btnGd = new GridData(50, 60);
        btnGd.verticalAlignment = SWT.END;
        button.setLayoutData(btnGd);

        button.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                ConnectionWizardPage.this.getDialogFileName();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }

        });
        return text;
    }

    /**
     * 返回文件对话框选择的文件名称
     * 
     * @return
     */
    private String getDialogFileName() {
        String filename = jarDialog.open();
        String[] fileNames = jarDialog.getFileNames();

        if (filename != null) {

            int lastindex = filename.lastIndexOf("\\");
            if (lastindex == -1) {
                lastindex = filename.lastIndexOf("/");
            }
            String path = filename.substring(0, lastindex + 1);

            String jars = driverJar.getText().trim();
            for (String fname : fileNames) {

                filename = path + fname;
                if ("".equals(jars)) {
                    jars = filename;
                } else {
                    if (jars.indexOf(filename) == -1) {
                        jars += "\n" + filename;
                    }
                }
            }
            driverJar.setText(jars);
        }
        return filename;
    }

    /**
     * 创建驱动class
     * 
     * @param parent
     * @param capital
     * @param gd
     * @return
     */
    private Combo createDriverTypeCombo(Composite parent, String capital, GridData gd) {
        Label label = new Label(parent, SWT.NULL);
        label.setText(capital);
        Combo combo = new Combo(parent, SWT.NULL);
        combo.setItems(DbConfigHelper.getDriveClasses());
        combo.setLayoutData(gd);
        // 添加事件监听
        combo.addSelectionListener(new DriveTypeChangeListener());

        return combo;
    }

    public boolean performFinish() {

        if (this.name.getText().isEmpty()) {
            MessageDialog.openInformation(JCodeSmithActivator.getActiveWorkbenchShell(), "error",
                    this.name.getToolTipText());
            this.name.forceFocus();
            return false;
        }

        if (DatabaseConfigManager.getConfigMgr().get(name.getText())!=null && !isEdit) {
            MessageDialog.openInformation(JCodeSmithActivator.getActiveWorkbenchShell(), "error",
                    "name already exists");
            this.name.forceFocus();
            return false;
        }
        
        if (this.driver.getText().isEmpty()) {
            MessageDialog.openInformation(JCodeSmithActivator.getActiveWorkbenchShell(), "error",
                    this.driver.getToolTipText());
            this.driver.forceFocus();
            return false;
        }

        if (this.url.getText().isEmpty()) {
            MessageDialog.openInformation(JCodeSmithActivator.getActiveWorkbenchShell(), "error",
                    this.url.getToolTipText());
            this.url.forceFocus();
            return false;
        }

        // 设置到databaseConfig
        setDatabaseConfig();

        // if (this.databaseMeta.getDatabaseType() == null &&
        // StringHelper.isBlankOrNull(dbType.getText()) == false) {
        // PluginLogger.openError("DbType can only be:" +
        // StringHelper.merger(DatabaseType.types(), ';'));
        // return false;
        // }
        // if (this.databaseMeta.validate() == false) {
        // PluginLogger.openError("Required field missing");
        // return false;
        // }
        //
        // if (isEdit) {
        // DatabaseView.refresh();
        // } else {
        // DatabaseView.addConnection(this.databaseMeta);
        // }
        return true;
    }

    private void setDatabaseConfig() {
        if (databaseConfig == null) {
            databaseConfig = new DatabaseConfig();
        }
        databaseConfig.setManulName(name.getText().trim());
        // password!
        databaseConfig.setUrl(url.getText().trim());
        databaseConfig.setDriverClazz(driver.getText().trim());
        databaseConfig.setDriverJars(driverJar.getText().trim());
    }

    /**
     * 驱动类型变更监听
     * 
     * @author DELL
     * 
     */
    class DriveTypeChangeListener implements SelectionListener {

        @Override
        public void widgetSelected(SelectionEvent e) {

            String dclass = driver.getText().trim();
            url.setText(DbConfigHelper.getJdbcUrlByDrive(dclass));
            driverJar.setText(DbConfigHelper.getJarByDrive(dclass));
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            // TODO Auto-generated method stub

        }

    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

}
