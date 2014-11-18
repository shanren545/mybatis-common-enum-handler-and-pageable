package com.shanren;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * 扩展默认的MyBatis工厂类，自动扫描指定的所有包前缀下实现IEnum接口的枚举类，并对其注册类型处理器EnumValueTypeHandler
 * 
 * @author xianwen.tan
 *
 */
public class MybatisSqlSessionFactoryBean extends SqlSessionFactoryBean {

	/**
	 * 指定需扫描的枚举类所在包的前缀，可以指定多个包，会自动扫描所有子包。分隔符与spring的包路径分隔符兼容。
	 */
	protected String enumBasePackages;

	protected static final ConcurrentHashMap<Class<? extends IEnum>, EnumValueTypeHandler<?>> TYPE_HANDLER_CACHE = new ConcurrentHashMap<Class<? extends IEnum>, EnumValueTypeHandler<?>>();

	public void setEnumBasePackages(String enumBasePackages) {
		this.enumBasePackages = enumBasePackages;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		TypeHandlerRegistry registry = getObject().getConfiguration().getTypeHandlerRegistry();

		String[] packages = parseEnumBasePackage();
		if (null == packages) {
			return;
		}

		Set<Class<? extends IEnum>> enumClasses = doScanEnumClass(packages);
		if (null != enumClasses) {
			for (Class<? extends IEnum> cls : enumClasses) {
				registry.register(cls, getEnumValueTypeHandlerInstance(cls));// 显示注册枚举处理器
			}
		}
	}

	/**
	 * 获取枚举对应的handler实例，获取后该枚举对应的handle实例被缓存起来
	 * 
	 * @param enumClass
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected EnumValueTypeHandler getEnumValueTypeHandlerInstance(Class<? extends IEnum> enumClass) {
		if (TYPE_HANDLER_CACHE.containsKey(enumClass)) {
			return TYPE_HANDLER_CACHE.get(enumClass);
		}

		EnumValueTypeHandler<?> handler = new EnumValueTypeHandler(enumClass);
		TYPE_HANDLER_CACHE.putIfAbsent(enumClass, handler);
		return handler;
	}

	/**
	 * 搜索实现IEnum接口的枚举类
	 * 
	 * @param enumBasePackages
	 * @return
	 */
	protected Set<Class<? extends IEnum>> doScanEnumClass(String... enumBasePackages) {

		Set<Class<? extends IEnum>> filterdClasses = new HashSet<Class<? extends IEnum>>();

		ResolverUtil<IEnum> resolverUtil = new ResolverUtil<IEnum>();
		resolverUtil.findImplementations(IEnum.class, enumBasePackages);
		Set<Class<? extends IEnum>> handlerSet = resolverUtil.getClasses();
		for (Class<? extends IEnum> type : handlerSet) {
			if (type.isEnum()) {
				filterdClasses.add(type);
			}
		}

		return filterdClasses;
	}

	protected String[] parseEnumBasePackage() {
		return StringUtils.tokenizeToStringArray(this.enumBasePackages, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
	}

}
