/**
 * 
 */
package org.jcodesmith.ui.db.views;

import java.sql.Connection;
import java.util.List;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.jcodesmith.db.config.DatabaseConfig;
import org.jcodesmith.db.config.DatabaseConfigManager;
import org.jcodesmith.db.dal.ConnectionManager;
import org.jcodesmith.db.dal.MetaManager;
import org.jcodesmith.db.meta.TableMeta;
import org.jcodesmith.plugin.helper.PluginLogger;
import org.jcodesmith.ui.db.action.DatabaseViewActionGroup;
import org.jcodesmith.ui.db.tree.BaseTreeNode;
import org.jcodesmith.ui.db.tree.DatabaseNode;
import org.jcodesmith.ui.db.tree.DefaultTreeViewContentProvider;
import org.jcodesmith.ui.db.tree.DefaultViewLabelProvider;
import org.jcodesmith.ui.db.tree.TableNode;

/**
 * 数据库连接的视图
 * 
 * @author DELL
 * 
 */
public class DataBaseView extends ViewPart {

    public static String ID = "org.jcodesmith.db.views.DataBaseExplorer";
    public static String MENU_ID = "#" + ID;

    private static DataBaseView instance;

    private static TreeViewer viewer;


    private DatabaseViewActionGroup actionGroup;
    
    /**
     * 树创建
     */
    private static DefaultTreeViewContentProvider dbTreeProvider;

    public TreeViewer getViewer() {
        return viewer;
    }

    public synchronized static DataBaseView getInstance() {
        if (instance == null) {
            instance = new DataBaseView();
        }
        return instance;
    }

    /**
     * 根据表名查找连接名
     * @param tableName
     * @return
     */
    public static String getDByTableName(String tableName){
        BaseTreeNode root=dbTreeProvider.getDataInputRoot();
        for (BaseTreeNode n : root.getChildrenList()) {
            if(n.hasChildren()){
               for (BaseTreeNode t : n.getChildrenList()) {
                   if(t.getName().equals(tableName)){
                       return n.getName();
                   }
               }
                
            }
        }
        return null;
    }
    
    
    /**
     * 根据树选中的节点，获取连接
     * 
     * @return
     */
    public static Connection getSelectConnection() {
        try {
            DatabaseNode dbnode = getSelectDataBaseNode();
            Connection conn = ConnectionManager.getConnection(dbnode.getDatabaseConfig(), true);
            if (conn != null) {
                dbnode.setActived(true);
            }
            return conn;
        } catch (Exception e) {
            PluginLogger.openError(e);
            return null;
        }
    }

    /**
     * 根据树选中的节点，获取连接
     * 
     * @return
     */
    public static Connection getSelectConnection(DatabaseNode dbnode) {
        try {
            Connection conn = ConnectionManager.getConnection(dbnode.getDatabaseConfig(), true);
            if (conn != null) {
                dbnode.setActived(true);
            }
            return conn;
        } catch (Exception e) {
            PluginLogger.openError(e);
            return null;
        }
    }

    public static TableNode getSelectTableNode(){
        BaseTreeNode selectNode = (BaseTreeNode) ((StructuredSelection) viewer.getSelection()).getFirstElement();
        if(selectNode ==null || !(selectNode instanceof TableNode)){
            return null;
        }else{
            return (TableNode)selectNode;
        }
    }
    
    /**
     * 获取选中
     * 
     * @return
     */
    public static DatabaseNode getSelectDataBaseNode() {
        BaseTreeNode selectNode = (BaseTreeNode) ((StructuredSelection) viewer.getSelection()).getFirstElement();
        DatabaseNode dbnode = getRootDatabaseModel(selectNode);
        return dbnode;
    }

    /**
     * 返回树中根节点ConnectionModel
     * 
     * @param model
     * @return
     */
    public static DatabaseNode getRootDatabaseModel(BaseTreeNode node) {
        BaseTreeNode model = node;
        while (!(model instanceof DatabaseNode) && model != null) {
            model = model.getParent();
        }
        return (DatabaseNode) model;
    }

    /**
     * 打开连接
     */
    public static void connectAndListTable() {
        
        DatabaseNode dbnode = getSelectDataBaseNode();
        dbnode.clear();
        Connection conn = getSelectConnection(dbnode);
        if (conn == null) {
            return;
        }
        List<TableMeta> tables = MetaManager.getTables(conn,false);
        for (TableMeta tbMeta : tables) {
            TableNode tnode = new TableNode(tbMeta);
            dbnode.addChild(tnode);
        }
        viewer.refresh();
    }

    /**
     * 初始化视图
     */
    @Override
    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        dbTreeProvider = new DefaultTreeViewContentProvider(this);
        viewer.setContentProvider(dbTreeProvider);

        viewer.setLabelProvider(new DefaultViewLabelProvider());
        viewer.setInput(dbTreeProvider.getDataInputRoot());
        viewer.setSorter(new ViewerSorter());
        viewer.setInput(getViewSite());

        actionGroup=new DatabaseViewActionGroup();
        
        contributeToActionBars();
        
        hookContextMenu();
        //加载配置
      String[] list=DatabaseConfigManager.getConfigMgr().listAllKey();
        for (String name : list) {
            addConnection(DatabaseConfigManager.getConfigMgr().get(name),false);
        }
        
    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub

    }

    /**
     * 在connection视图中增加一条连接
     * 
     * @param connectionModel
     */
    private static void addConnection(DatabaseConfig config,boolean addToManagers ) {
        if(addToManagers){
            DatabaseConfigManager.getConfigMgr().put(config);
        }
        if(viewer!=null && !viewer.getTree().isDisposed()){
            DatabaseNode databaseNode = new DatabaseNode(config);
            dbTreeProvider.getDataInputRoot().addChild(databaseNode);
            viewer.refresh();
        }
    
    }
    
    /**
     * 删除
     * 
     * @param connectionModel
     */
    public static void deleteConnection(DatabaseConfig config) {
        DatabaseConfigManager.getConfigMgr().remove(config);
        if(viewer!=null){ 
            for (BaseTreeNode node : dbTreeProvider.getDataInputRoot().getChildrenList()) {
                if(node.getName().equals(config.getManulName())){
                    dbTreeProvider.getDataInputRoot().removeChild(node);
                    break;
                }
            }
            viewer.setInput(dbTreeProvider.getDataInputRoot());
        }
    }
    /**
     * 在connection视图中增加一条连接
     * 
     * @param connectionModel
     */
    public static void addConnection(DatabaseConfig config) {
        addConnection(config,true);
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                actionGroup.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        actionGroup.fillActionBars(bars);
    }




}
