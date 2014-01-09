package org.jcodesmith.db.meta;

/**
 * 数据库列元数据
 * 
 * @author greki.shen
 * 
 */
@SuppressWarnings("rawtypes")
public class ColumnMeta {

    /**
     * 列名称
     */
    private String name;
    /**
     * 列类型
     */
    private int sqlType;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 对应的java类型
     */
    private Class javaType;
    /**
     * 列长度
     */
    private int columnSize;
    /**
     * 精度
     */
    private int decimalDigits;
    /**
     * 是否允许为空
     */
    private boolean isNullable;

    /**
     * 注解
     */
    private String comment;
    /**
     * 是否主键
     */
    private boolean primaryKey = false;
    /**
     * 是否外键
     */
    private boolean foreignKey = false;
    
    
    private TableMeta table;

    public String getName() {
        return name;
    }

    public void setName(String columnName) {
        this.name = columnName;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Class getJavaType() {
        return javaType;
    }

    public void setJavaType(Class javaType) {
        this.javaType = javaType;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean isNullable) {
        this.isNullable = isNullable;
    }

    public void setNullable(String isNullable) {
        this.isNullable = "NO".equalsIgnoreCase(isNullable) == false;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }

    public boolean isStringClass() {
        return this.getJavaType() == String.class;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TableMeta getTable() {
        return table;
    }

    public void setTable(TableMeta tableMeta) {
        this.table = tableMeta;
    }

}
