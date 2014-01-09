package org.jcodesmith.ui.db.action;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionGroup;
import org.jcodesmith.ui.db.tree.DatabaseNode;
import org.jcodesmith.ui.db.views.DataBaseView;

public class DatabaseViewActionGroup extends ActionGroup {
    /**
     * 添加连接
     */
    private EditConnectionAction addconAction;
    
    /**
     * 编辑连接
     */
    private EditConnectionAction editConAction;
    
    /**
     * 打开连接
     */
    private ConnectionAction connectionAction;
    
    
    private DeleteConnectionAction deleteAction;
    
    public DatabaseViewActionGroup(){
        
        addconAction=new EditConnectionAction(false);
 
        
        editConAction=new EditConnectionAction(true);
        
        
        connectionAction=new ConnectionAction();

        deleteAction=new DeleteConnectionAction();
        
    }
    
    
    /* (non-Javadoc)
     * Method declared in ActionGroup
     */
    @Override
    public void fillContextMenu(IMenuManager menu) {
        
        menu.add(addconAction);
        
        DatabaseNode dbnode=DataBaseView.getSelectDataBaseNode();
        if(dbnode!=null){
            menu.add(connectionAction);
            
            editConAction.setDatabaseConfig(dbnode.getDatabaseConfig());
            menu.add(editConAction);
            
            deleteAction.setDatabaseConfig(dbnode.getDatabaseConfig());
            menu.add(deleteAction);
        }
        super.fillContextMenu(menu);
    }

    
    /* (non-Javadoc)
     * Method declared in ActionGroup
     */
    @Override
    public void setContext(ActionContext context) {
        super.setContext(context);
    }
    
    /* (non-Javadoc)
     * Method declared in ActionGroup
     */
    @Override
    public void updateActionBars() {
        super.updateActionBars();
    }
    
    
    public void fillActionBars(IActionBars actionBars) {
        actionBars.getMenuManager().add(addconAction);
    }
  
    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        super.dispose();
    }
    
}
