package org.jcodesmith.db.meta;

import java.util.List;

import org.jcodesmith.db.dal.DataBaseType;

/**
 * 数据库信息
 * 
 * @author greki.shen
 * 
 */
public class DatabaseMeta {

    private String name;
    
    private DataBaseType databaseType;
    
    private String manulName = "";
    
    private List<TableMeta> tableList;
    
   public  DatabaseMeta(String name,String manulName,DataBaseType databaseType){
       this.name=name;
       this.manulName=manulName;
       this.databaseType=databaseType;
   }
   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataBaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DataBaseType databaseType) {
        this.databaseType = databaseType;
    }

    public List<TableMeta> getTableList() {
        return tableList;
    }

    public void setTableList(List<TableMeta> tableList) {
        this.tableList = tableList;
    }

    public String getManulName() {
        return manulName;
    }

    public void setManulName(String manulName) {
        this.manulName = manulName;
    }
   
    

}
