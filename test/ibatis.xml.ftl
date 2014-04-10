<@property name="package" desc="类的包名" type="String" defaultValue="com.greenline"/>
<@property name="table" desc="表" type="table" defaultValue=""/>
<@property name="author" desc="作者" type="String" defaultValue="author"/>
<@property name="doQuery" desc="是否生成查询" type="boolean" defaultValue="false"/>
<#assign aliasVo="${comUtil.nameSpace(table)}DO">

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE sqlMap PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN" "http://ibatis.apache.org/dtd/sql-map-2.dtd" >
<sqlMap namespace="${comUtil.nameSpace(table)}" >

	<typeAlias alias="${aliasVo}" type="${package}.${comUtil.className(table)}DO" />
	
	<resultMap id="baseResult" class="${aliasVo}">
	<#list table.columns as col>
 		  <result property="${comUtil.propertyName(col)}" column="${col.name}" />
 	</#list>
  	</resultMap>
  	
  	<sql id="column_List" >
    ${comUtil.getColumsString(table)}
  	</sql>
  
  	<!--table insert-->
	<insert id="insert" parameterClass="${aliasVo}" >
		${comUtil.getInsertString(table)}
	    <!--
	    <selectKey resultClass="int" keyProperty="id">
	     	<![CDATA[SELECT LAST_INSERT_ID() AS ID ]]>
	    </selectKey>
	    -->
	</insert>

	<!--table update-->
	<update id="update" parameterClass="${aliasVo}" >
    	update ${table.name} set gmt_modified = ${comUtil.getSqlNowStr(table)}
		<dynamic prepend="" >
			<#list table.columns as col>
			 <#if col.name!="id">
 			<isNotEmpty prepend="," property="${comUtil.propertyName(col)}">
					${col.name} = #${comUtil.propertyName(col)}#
			</isNotEmpty>
			</#if>
 			</#list>
		</dynamic>
    	where id = #id#
	</update>
  
    <!--find_by_id-->
  	<select id="find_by_id" resultMap="baseResult" parameterClass="java.lang.Long" >
		select 
	    <include refid="column_List" />
	    from ${table.name}
	    where id = #id# 
	</select>
	
    <!--delete-->
  	<delete id="delete" parameterClass="java.lang.Long" >
    	delete from ${table.name}
    	where id = #id#
	</delete>
  
	
  	<#if doQuery>
  	<!--query-->
  	<!--
  	
	<#assign aliasQ="${comUtil.nameSpace(table)}DO">
	<typeAlias alias="${aliasQ}" type="${package}.${comUtil.className(table)}DO" />
	<sql id="query_condition">
	  <dynamic prepend="where">
			<#list table.columns as col>
 			<isNotEmpty prepend="AND" property="${comUtil.propertyName(col)}" removeFirstPrepend="true">
					${col.name} = #${comUtil.propertyName(col)}#
			</isNotEmpty>
 			</#list>
      </dynamic>
	</sql>
      

  
	<select id="list_by_query_count" parameterClass="${aliasQ}" resultClass="java.lang.Integer" >
  		select count(*) from ${table.name}
   		<include refid="query_condition" />
	</select>
	
	<#if comUtil.getDBType(table)=="oracle">
	<sql id="paging_start">
     	SELECT T2.*
			FROM (  
				SELECT T1.*, ROWNUM RNUM
				FROM (
	</sql>
	<sql id="paging_end">
		<![CDATA[
					)T1
					WHERE ROWNUM <=#endPos# 
				) T2
			WHERE RNUM >=#startPos# 
		]]>
	</sql>
	<select id="list_by_query" resultMap="baseResult" parameterClass="${aliasQ}" >
       <include refid="$paging_start"/>
        select
        <include refid="column_List" />
        from ${table.name}
        <include refid="query_condition" />
        <include refid="paging_end"/>
    </select>
    
    <#else>
	
	<sql id="limit_condition">
	 	<![CDATA[
			ORDER BY id desc
		]]>
		LIMIT #startPos#,#pageSize#
  	</sql>
	<select id="list_by_query" resultMap="baseResult" parameterClass="${aliasQ}" >
		select
		<include refid="column_List" />
		from ${table.name}
		<include refid="query_condition" />
		<include refid="limit_condition" />
  	</select>
  	</#if>
  	
   	-->
   </#if>
   
</sqlMap>
