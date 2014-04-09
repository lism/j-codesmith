<@property name="package" desc="类的包名" type="String" defaultValue="com.greenline"/>
<@property name="tables" desc="表" type="tables" defaultValue=""/>
<@property name="author" desc="作者" type="String" defaultValue="author"/>
<@property name="savePath" desc="保持路径" type="dir" defaultValue="author"/>

<#list tables as tbl>
  ${outUtil.write(savePath+"\\"+tbl.name+"_sqlmapping.xml","ibatis.xml.ftl",package,tbl,author,false)}
  ${tbl.name}已生成
</#list>
