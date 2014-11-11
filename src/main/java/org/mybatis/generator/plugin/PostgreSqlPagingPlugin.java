package org.mybatis.generator.plugin;

import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.jingwei.PageInfo;

/**
 * PostgreSQL数据库的分页插件
 * @author xianwen.tan
 *
 */
public class PostgreSqlPagingPlugin extends PluginAdapter {

	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		appendPageInfoAfterSelectByExampleXml(element, introspectedTable);
		return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element,
				introspectedTable);
	}

	@Override
	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		appendPageInfoAfterSelectByExampleXml(element, introspectedTable);
		return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element,
				introspectedTable);
	}

	private void appendPageInfoAfterSelectByExampleXml(XmlElement element,
			IntrospectedTable introspectedTable) {

		XmlElement pageEl = new XmlElement("if");
		pageEl.addAttribute(new Attribute("test", "pageInfo != null"));
		pageEl.addElement(new TextElement(
				" limit ${pageInfo.pageSize}  offset ${pageInfo.offset} "));
		element.addElement(pageEl);
	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		// add field, getter, setter for page info
		addPageInfoToExampleClass(topLevelClass, introspectedTable);
		return super.modelExampleClassGenerated(topLevelClass,
				introspectedTable);
	}

	private void addPageInfoToExampleClass(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		topLevelClass.addImportedType(new FullyQualifiedJavaType(PageInfo.class
				.getName()));

		CommentGenerator commentGenerator = context.getCommentGenerator();
		Field field = new Field();
		field.setVisibility(JavaVisibility.PROTECTED);
		field.setType(new FullyQualifiedJavaType(PageInfo.class.getName()));
		field.setName("pageInfo");
		// field.setInitializationString("null");

		commentGenerator.addFieldComment(field, introspectedTable);
		topLevelClass.addField(field);

		// add setter method
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("setPageInfo");
		method.addParameter(new Parameter(new FullyQualifiedJavaType(
				PageInfo.class.getName()), "pageInfo"));
		method.addBodyLine("this.pageInfo = pageInfo;");
		method.addBodyLine("if(null != pageInfo){");
		method.addBodyLine("orderByClause = pageInfo.getSort();");
		method.addBodyLine("}");
		commentGenerator.addGeneralMethodComment(method, introspectedTable);
		topLevelClass.addMethod(method);

		// add getter method
		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType(PageInfo.class
				.getName()));
		method.setName("getPageInfo");
		method.addBodyLine("return pageInfo;");
		commentGenerator.addGeneralMethodComment(method, introspectedTable);
		topLevelClass.addMethod(method);
	}

	/**
	 * This plugin is always valid - no properties are required
	 */
	public boolean validate(List<String> warnings) {
		return true;
	}

	public static void generate() {
		String config = PostgreSqlPagingPlugin.class.getClassLoader()
				.getResource("generatorConfig.xml").getFile();
		String[] arg = { "-configfile", config, "-overwrite" };
		ShellRunner.main(arg);
	}

	public static void main(String[] args) {
		generate();
	}
}
