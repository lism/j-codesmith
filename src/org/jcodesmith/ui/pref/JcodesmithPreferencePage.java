package org.jcodesmith.ui.pref;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.jcodesmith.JCodeSmithActivator;
import org.jcodesmith.plugin.helper.PluginSetting;

public class JcodesmithPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private TableViewer variableTable;
    protected Button envAddButton;
    protected Button envEditButton;
    protected Button envRemoveButton;
    
    protected SimpleVariableContentProvider variableContentProvider= new SimpleVariableContentProvider();
    
    
    protected static String[] variableTableColumnProperties= {
        "variable", 
        "value",
        "description" 
    };
    
    protected String[] variableTableColumnHeaders= {
           "Variable Name", 
           "Class Full Name",
           "Description"
        };
    
    protected ColumnLayoutData[] variableTableColumnLayouts= {
            new ColumnWeightData(30),
            new ColumnWeightData(45),
            new ColumnWeightData(25),
        };
    
    public JcodesmithPreferencePage() {
        super(GRID);
        setTitle("JCodeSmith Preference");
        setPreferenceStore(JCodeSmithActivator.getDefault().getPreferenceStore());
        
        
 }
    
    @Override
    public void init(IWorkbench workbench) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void createFieldEditors() {
        Composite composite = new Composite(getFieldEditorParent(), SWT.NORMAL);
        
        GridLayout basicLayout = new GridLayout();
        basicLayout.numColumns = 1;
        composite.setLayout(basicLayout);
        
        
        
        GridData gdc = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gdc);
        
        
        FilesEditor fileEditor=      new FilesEditor(PluginSetting.PREFERENCE_JAR, "Extends Util Jars","Extends Util Jars", composite);
        fileEditor.setFileExtensions(new String[] { "*.jar", "*.zip", "*.*" });
        fileEditor.setFileExtensionNames(new String[] { "Jar Files (*.jar)", "Zip Files (*.zip)", "All Files (*.*)" });
        //jar
        addField(fileEditor);
        
        
       // IWorkbenchPreferenceContainer container= (IWorkbenchPreferenceContainer) getContainer();
        //fConfigurationBlock= new NameConventionConfigurationBlock(getNewStatusChangedListener(), getProject(), container);
        
        createTable(composite);
        
        createButtons(composite);
   
    }
    
    
    /**
     * Creates and configures the table containing launch configuration variables
     * and their associated value.
     */
    private void createTable(Composite parent) {
        Font font= parent.getFont();
        // Create table composite
        Composite tableComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.numColumns = 1;
        GridData gridData = new GridData(GridData.FILL_BOTH);
        tableComposite.setLayout(layout);
        tableComposite.setLayoutData(gridData);
        tableComposite.setFont(font);
        // Create table
        variableTable = new TableViewer(tableComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
        Table table = variableTable.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setFont(font);
        
        gridData = new GridData(GridData.FILL_BOTH);
        variableTable.getControl().setLayoutData(gridData);
        variableTable.setContentProvider(variableContentProvider);
        variableTable.setColumnProperties(variableTableColumnProperties);
        variableTable.addFilter(new VariableFilter());
//        variableTable.setComparator(new ViewerComparator() {
//            public int compare(Viewer iViewer, Object e1, Object e2) {
//                if (e1 == null) {
//                    return -1;
//                } else if (e2 == null) {
//                    return 1;
//                } else {
//                    return ((VariableWrapper)e1).getName().compareToIgnoreCase(((VariableWrapper)e2).getName());
//                }
//            }
//        });
        
        variableTable.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                //handleTableSelectionChanged(event);
            }
        });
        
        variableTable.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                if (!variableTable.getSelection().isEmpty()) {
                   // handleEditButtonPressed();
                }
            }
        });
        variableTable.getTable().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                if (event.character == SWT.DEL && event.stateMask == 0) {
                    //handleRemoveButtonPressed();
                }
            }
        });
    
        for (int i = 0; i < variableTableColumnHeaders.length; i++) {
            TableColumn tc = new TableColumn(table, SWT.NONE, i);
            tc.setResizable(variableTableColumnLayouts[i].resizable);
            tc.setText(variableTableColumnHeaders[i]);
            tc.setWidth(50);
        }
        
        // Try restoring column widths from preferences, if widths aren't stored, init columns to default
       // if (!restoreColumnWidths()){
           // restoreDefaultColumnWidths();
      //  }
        
      //  variableTable.setInput(getVariableManager());
       // variableTable.setLabelProvider(new SimpleVariableLabelProvider());
        
        
        
        this.editor();
    }

    protected void addField(FieldEditor editor) {
        super.addField(editor);
    }
    
    
    /**
     * Creates the new/edit/remove buttons for the variable table
     * @param parent the composite in which the buttons should be created
     */
    private void createButtons(Composite parent) {
        // Create button composite
        Composite buttonComposite = new Composite(parent, SWT.NONE);
        GridLayout glayout = new GridLayout();
        glayout.marginHeight = 0;
        glayout.marginWidth = 0;
        glayout.numColumns = 1;
        GridData gdata = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        gdata.horizontalSpan=1;
        buttonComposite.setLayout(glayout);
        buttonComposite.setLayoutData(gdata);
        buttonComposite.setFont(parent.getFont());
        
        // Create buttons
        envAddButton = new Button(buttonComposite, SWT.BUTTON1); 
        envAddButton.setLayoutData(gdata);
        envAddButton.setText("ddddd");
        envAddButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent event) {
             //   handleAddButtonPressed();
            }
        });
//        envEditButton = SWTFactory.createPushButton(buttonComposite, DebugPreferencesMessages.SimpleVariablePreferencePage_8, null); 
//        envEditButton.addSelectionListener(new SelectionAdapter()
//        {
//            public void widgetSelected(SelectionEvent event) {
//                handleEditButtonPressed();
//            }
//        });
//        envEditButton.setEnabled(false);
//        envRemoveButton = SWTFactory.createPushButton(buttonComposite, DebugPreferencesMessages.SimpleVariablePreferencePage_9, null); 
//        envRemoveButton.addSelectionListener(new SelectionAdapter()
//        {
//            public void widgetSelected(SelectionEvent event) {
//                handleRemoveButtonPressed();
//            }
//        });
       // envRemoveButton.setEnabled(false);
    }
    
    private void editor() {
        CellEditor[] editors = new CellEditor[4];
        editors[0] = new TextCellEditor(variableTable.getTable());
        editors[1] = new TextCellEditor(variableTable.getTable());
        editors[2] = new TextCellEditor(variableTable.getTable());
 
        variableTable.setCellEditors(editors);
        variableTable.setCellModifier(new TableCellModifier());
    }
    
    class VariableFilter extends ViewerFilter {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            return !((VariableWrapper)element).isRemoved();
        }
        
    }
    
    /**
     * @description: 编辑单元格内容
     * @author Administrator
     * @version 1.0, 2010-2-19
     * @Copyright 2010-2020
     */
    public class TableCellModifier implements ICellModifier{
     
        public boolean canModify(Object element, String property) {
            // TODO Auto-generated method stub
            return true;
        }
     
        public Object getValue(Object element, String property) {
            // TODO Auto-generated method stub
            return "...";
        }
     
        public void modify(Object element, String property, Object value) {
            // TODO Auto-generated method stub
           
        }
       
    }
    
class VariableWrapper {
        
        protected String fNewName = null;
        protected String fNewDesc = null;
        protected String fNewValue = null;
        boolean fRemoved = false;
        boolean fAdded = false;
        
        
        public VariableWrapper(String name, String desc, String value) {
            fNewName = name;
            fNewDesc = desc;
            fNewValue = value;
            fAdded = true;
        }
        
        public boolean isAdded() {
            return fAdded;
        }
        
        public String getName() {
            if (fNewName == null) {
            }
            return fNewName;
        }
        
        public void setName(String name) {
            fNewName = name;
        }
        
        public String getDescription() {
            if (fNewDesc == null) {
            }
            return fNewDesc;
        }
        
        public String getValue() {
            if (fNewValue == null) {
            }
            return fNewValue;
        }
        
        public void setValue(String value) {
            fNewValue = value;
        }
        
        public void setDescription(String desc) {
            fNewDesc = desc;
        }
        
        public boolean isChanged() {
            return !fAdded && !fRemoved && (fNewValue != null || fNewDesc != null);
        }
        
        public boolean isRemoved() {
            return fRemoved;
        }
        
        public void setRemoved(boolean removed) {
            fRemoved = removed;
        }
    }
    private class SimpleVariableContentProvider implements IStructuredContentProvider {

        @Override
        public void dispose() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Object[] getElements(Object inputElement) {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
}
