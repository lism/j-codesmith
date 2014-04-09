<@property name="package" desc="类的包名" type="String" defaultValue="com.greenline"/>
<@property name="table" desc="表" type="table" defaultValue=""/>
<@property name="author" desc="作者" type="String" defaultValue="author"/>
${project_filecomment!}
package ${package};

${comUtil.classImportStr(table)}

/**
 *  ${table.name!} ${comUtil.deleteRN(table.comment)}<br/>
 *  ${comUtil.now()}<br/>
 *  @author ${author!}
 */
 public class ${comUtil.className(table)}DO{
 	<#list table.columns as col>
 	<#if col.comment?? && col.comment!="">//${comUtil.deleteRN(col.comment)}</#if>
 	private ${comUtil.propertyType(col)} ${comUtil.propertyName(col)};
 	</#list>
 	<#list table.columns as col>
 	
 	<#if col.comment?? && col.comment!="">
 	/**
	* ${comUtil.deleteRN(col.comment)} 
	*
	* @return ${comUtil.propertyName(col)}
	*/
	</#if>
	public ${comUtil.propertyType(col)} get${comUtil.propertyMethodName(col)}(){
		return ${comUtil.propertyName(col)};
	}
 	
 	<#if col.comment?? && col.comment!="">
 	/**
	* ${comUtil.deleteRN(col.comment)} 
	*
	* @param ${comUtil.propertyName(col)}
	*/
	</#if>
	public void  set${comUtil.propertyMethodName(col)}(${comUtil.propertyType(col)} ${comUtil.propertyName(col)}){
		this.${comUtil.propertyName(col)}=${comUtil.propertyName(col)};
	}
 	</#list>
 	
 }