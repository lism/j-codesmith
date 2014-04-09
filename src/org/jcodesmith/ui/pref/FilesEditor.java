package org.jcodesmith.ui.pref;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.List;

public class FilesEditor extends ListEditor {

    /**
     * The last path, or <code>null</code> if none.
     */
    private String lastPath;

    /**
     * The special label text for files chooser, 
     * or <code>null</code> if none.
     */
    private String filesChooserLabelText;
    
    
    private String[] fileExtensions;
    private String[] fileExtensionNames;
    
    public String[] getFileExtensions() {
        return fileExtensions;
    }

    public void setFileExtensions(String[] fileExtensions) {
        this.fileExtensions = fileExtensions;
    }

    public String[] getFileExtensionNames() {
        return fileExtensionNames;
    }

    public void setFileExtensionNames(String[] fileExtensionNames) {
        this.fileExtensionNames = fileExtensionNames;
    }

    /**
     * Creates a new path field editor 
     */
    protected FilesEditor() {
    }

    /**
     * Creates a path field editor.
     * 
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param filesChooserLabelText the label text displayed for the directory chooser
     * @param parent the parent of the field editor's control
     */
    public FilesEditor(String name, String labelText,
            String dirChooserLabelText, Composite parent) {
        init(name, labelText);
        this.filesChooserLabelText = dirChooserLabelText;
        createControl(parent);
    }

    /* (non-Javadoc)
     * Method declared on ListEditor.
     * Creates a single string from the given array by separating each
     * string with the appropriate OS-specific path separator.
     */
    protected String createList(String[] items) {
        StringBuffer path = new StringBuffer("");//$NON-NLS-1$

        for (int i = 0; i < items.length; i++) {
            path.append(items[i]);
            path.append(File.pathSeparator);
        }
        return path.toString();
    }

    /* (non-Javadoc)
     * Method declared on ListEditor.
     * Creates a new path element by means of a directory dialog.
     */
    protected String getNewInputObject() {

        FileDialog dialog = new FileDialog(getShell(),  SWT.OPEN | SWT.MULTI);
        if (filesChooserLabelText != null) {
            dialog.setText(filesChooserLabelText);
        }
        dialog.setFilterExtensions(fileExtensions);
        dialog.setFilterNames(fileExtensionNames);
        if (lastPath != null) {
            if (new File(lastPath).exists()) {
                dialog.setFilterPath(lastPath);
            }
        }
        String dir = dialog.open();
        if (dir != null) {
            dir = dir.trim();
            if (dir.length() == 0) {
                return null;
            }
            lastPath = dir;
        }
        return dir;
    }

    /* (non-Javadoc)
     * Method declared on ListEditor.
     */
    protected String[] parseString(String stringList) {
        return parser(stringList);
    }
    
    public static String[] parser(String stringList){
        StringTokenizer st = new StringTokenizer(stringList, File.pathSeparator
                + "\n\r");//$NON-NLS-1$
        ArrayList<Object> v = new ArrayList<Object>();
        while (st.hasMoreElements()) {
            v.add(st.nextElement());
        }
        return (String[]) v.toArray(new String[v.size()]);
    }
    
    public String[] getItems(){
        List l=getList();
      return  l.getItems();
    }
}
