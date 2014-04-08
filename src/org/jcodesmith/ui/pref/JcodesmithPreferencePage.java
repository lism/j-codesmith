package org.jcodesmith.ui.pref;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jcodesmith.JCodeSmithActivator;
import org.jcodesmith.engine.CustomVariable;
import org.jcodesmith.engine.CustomVariableManager;
import org.jcodesmith.engine.ShareVariables;
import org.jcodesmith.plugin.helper.PluginSetting;
import org.jcodesmith.ui.dialog.CustomVarDialog;

public class JcodesmithPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private TableViewer variableTable;
    protected Button envAddButton;
    protected Button envEditButton;
    protected Button envRemoveButton;
    protected FilesEditor fileEditor ;
    protected SimpleVariableContentProvider contentProvider = new SimpleVariableContentProvider();

    protected static String[] variableTableColumnProperties = { "name", "className", "desc" };

    protected String[] variableTableColumnHeaders = { "Var Name", "Class Name", "Description" };

    protected int[] variableTableColumnWidth = { 80, 100, 120 };

    public JcodesmithPreferencePage() {
        super(GRID);
        setTitle("JCodeSmith Preference");
        setPreferenceStore(JCodeSmithActivator.getDefault().getPreferenceStore());
    }

    public boolean performOk() {
        CustomVariableManager.clear();
        //add extens jars
        if(fileEditor!=null){
            String[] jars=  fileEditor.getItems();
            for (String jar : jars) {
                CustomVariableManager.add(jar);
            }
        }
        //add Variable
        for (CustomVariable var : (CustomVariable[])contentProvider.getElements(null)) {
           if(!var.isSystem()){
               CustomVariableManager.add(var);
           }
        }
        return super.performOk();
    }
    
    protected void performDefaults() {
        contentProvider.setDefault();
        super.performDefaults();
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

        fileEditor = new FilesEditor(PluginSetting.PREFERENCE_JAR, "Extends Util Jars",
                "Extends Util Jars", composite);
        fileEditor.setFileExtensions(new String[] { "*.jar", "*.zip", "*.*" });
        fileEditor.setFileExtensionNames(new String[] { "Jar Files (*.jar)", "Zip Files (*.zip)", "All Files (*.*)" });

        // jar
        addField(fileEditor);
        createTable(composite);
        createButtons(composite);
    }

    /**
     * Creates and configures the table containing launch configuration
     * variables and their associated value.
     */
    private void createTable(Composite parent) {
        Font font = parent.getFont();
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
        variableTable = new TableViewer(tableComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI
                | SWT.FULL_SELECTION);
        Table table = variableTable.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setFont(font);

        gridData = new GridData(GridData.FILL_BOTH);
        variableTable.getControl().setLayoutData(gridData);
        variableTable.setContentProvider(contentProvider);
        variableTable.setLabelProvider(new SimpleVariableLabelProvider());
        variableTable.setColumnProperties(variableTableColumnProperties);
        variableTable.addFilter(new VariableFilter());
        variableTable.setComparator(new ViewerComparator() {
            public int compare(Viewer iViewer, Object e1, Object e2) {
                if (e1 == null) {
                    return -1;
                } else if (e2 == null) {
                    return 1;
                } else {
                    return ((CustomVariable) e1).getName().compareToIgnoreCase(((CustomVariable) e2).getName());
                }
            }
        });

        variableTable.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                handleTableSelectionChanged(event);
            }
        });

        variableTable.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                if (!variableTable.getSelection().isEmpty()) {
                    handleEditButtonPressed();
                }
            }
        });
        variableTable.getTable().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                if (event.character == SWT.DEL && event.stateMask == 0) {
                    handleRemoveButtonPressed();
                }
            }
        });

        for (int i = 0; i < variableTableColumnHeaders.length; i++) {
            TableColumn tc = new TableColumn(table, SWT.NONE, i);
            tc.setText(variableTableColumnHeaders[i]);
            tc.setWidth(variableTableColumnWidth[i]);
        }

        variableTable.setInput(CustomVariableManager.getVarList());

    }

    /**
     * Responds to a selection changed event in the variable table
     * 
     * @param event the selection change event
     */
    protected void handleTableSelectionChanged(SelectionChangedEvent event) {
        IStructuredSelection selection = ((IStructuredSelection) event.getSelection());
        CustomVariable variable = (CustomVariable) selection.getFirstElement();
        if (variable == null || variable.isSystem()) {
            envEditButton.setEnabled(false);
            envRemoveButton.setEnabled(false);
        } else {
            envEditButton.setEnabled(selection.size() == 1);
            envRemoveButton.setEnabled(selection.size() > 0);
        }

    }

    private void handleAddButtonPressed() {
        CustomVarDialog dialog = new CustomVarDialog(getShell(), null);
        if (dialog.open() == Window.OK) {
            if (dialog.getVariable() != null) {
                contentProvider.addVariable(dialog.getVariable());
                variableTable.refresh();
            }
        }
    }

    private void handleEditButtonPressed() {

        IStructuredSelection selection = (IStructuredSelection) variableTable.getSelection();
        CustomVariable variable = (CustomVariable) selection.getFirstElement();
        if (variable == null || variable.isSystem()) {
            return;
        }

        CustomVarDialog dialog = new CustomVarDialog(getShell(), variable);
        if (dialog.open() == Window.OK) {
            if (dialog.getVariable() != null) {
                variableTable.update(variable, null);
            }
        }
    }

    /**
     * Remove the selection variables.
     */
    private void handleRemoveButtonPressed() {
        IStructuredSelection selection = (IStructuredSelection) variableTable.getSelection();
        CustomVariable var = (CustomVariable) selection.getFirstElement();
        if (var == null || var.isSystem()) {
            return;
        }
        contentProvider.remove(var);
        variableTable.refresh();
    }

    protected void addField(FieldEditor editor) {
        super.addField(editor);
    }

    /**
     * Creates the new/edit/remove buttons for the variable table
     * 
     * @param parent the composite in which the buttons should be created
     */
    private void createButtons(Composite parent) {
        // Create button composite
        Composite buttonComposite = new Composite(parent, SWT.NONE);
        GridLayout glayout = new GridLayout();
        glayout.marginHeight = 0;
        glayout.marginWidth = 0;

        glayout.numColumns = 1;
        GridData gdata = new GridData(GridData.FILL_VERTICAL);
        gdata.widthHint = 92;
        buttonComposite.setLayout(glayout);
        buttonComposite.setLayoutData(gdata);

        // Create buttons
        envAddButton = new Button(buttonComposite, SWT.PUSH);
        gdata = new GridData(GridData.FILL_HORIZONTAL);
        envAddButton.setLayoutData(gdata);
        envAddButton.setText("New");
        envAddButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                handleAddButtonPressed();
            }
        });

        envEditButton = new Button(buttonComposite, SWT.PUSH);
        gdata = new GridData(GridData.FILL_HORIZONTAL);
        envEditButton.setLayoutData(gdata);
        envEditButton.setText("Edit");
        envEditButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                handleEditButtonPressed();
            }
        });

        envEditButton.setEnabled(false);

        envRemoveButton = new Button(buttonComposite, SWT.PUSH);
        gdata = new GridData(GridData.FILL_HORIZONTAL);
        envRemoveButton.setText("Delete");
        envRemoveButton.setLayoutData(gdata);
        envRemoveButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                handleRemoveButtonPressed();
            }
        });
        envRemoveButton.setEnabled(false);
    }

    class VariableFilter extends ViewerFilter {

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers
         * .Viewer, java.lang.Object, java.lang.Object)
         */
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            return true;
        }

    }

    private class SimpleVariableLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider {
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            if (element instanceof CustomVariable) {
                CustomVariable variable = (CustomVariable) element;
                switch (columnIndex) {
                case 0:
                    return variable.getName();
                case 1:

                    return variable.getClassName();
                case 2:
                    return variable.getDesc();
                }
            }
            return null;
        }

        @Override
        public Color getForeground(Object element) {
            if (element instanceof CustomVariable) {
                if (((CustomVariable) element).isSystem()) {
                    Display display = Display.getCurrent();
                    return display.getSystemColor(SWT.COLOR_DARK_BLUE);
                }
            }
            return null;
        }

        @Override
        public Color getBackground(Object element) {
            return null;
        }
    }

    private class SimpleVariableContentProvider implements IStructuredContentProvider {
        /**
         * The content provider stores variable wrappers for use during editing.
         */
        private List<CustomVariable> list = new ArrayList<CustomVariable>();
        
        public void setDefault(){
            init();
        }
        public SimpleVariableContentProvider() {
            init();
        }
        private void init(){
            list.clear();
            // add system variable
            for (Entry<String, Object> v : ShareVariables.entrySet()) {
                CustomVariable var = new CustomVariable();
                var.setName(v.getKey());
                var.setClassName(v.getValue().getClass().getName());
                var.setSystem(true);
                var.setDesc("built in variable");
                list.add(var);
            }

            list.addAll(CustomVariableManager.getVarList());
        }
        @Override
        public Object[] getElements(Object inputElement) {
            return list.toArray();
        }

        /**
         * Adds the given variable to the 'wrappers'
         * 
         * @param variable variable to add
         */
        public void addVariable(CustomVariable variable) {
            list.add(variable);
        }

        public void remove(CustomVariable variable) {
            list.remove(variable);
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        /**
         * Saves the edited variable state to the variable manager.
         */
        public void saveChanges() {
            CustomVariableManager.setVarList(list);
        }

    }
}
