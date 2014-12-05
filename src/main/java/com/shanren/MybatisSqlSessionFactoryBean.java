package com.shanren;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/**
 * 扩展默认的MyBatis工厂类，自动扫描指定的所有包前缀下实现IEnum接口的枚举类，并对其注册类型处理器EnumValueTypeHandler
 * 
 * @author xianwen.tan
 */
public class MybatisSqlSessionFactoryBean extends SqlSessionFactoryBean {

    private static final Log logger = LogFactory.getLog(MybatisSqlSessionFactoryBean.class);

    /**
     * 指定需扫描的枚举类所在包的前缀，可以指定多个包，会自动扫描所有子包。分隔符与spring的包路径分隔符兼容。
     */
    protected String enumBasePackages;

    protected SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

    protected Resource[] mapperLocations;

    protected static final ConcurrentHashMap<Class<? extends IEnum>, EnumValueTypeHandler<?>> TYPE_HANDLER_CACHE =
            new ConcurrentHashMap<Class<? extends IEnum>, EnumValueTypeHandler<?>>();

    @Override
    public void setSqlSessionFactoryBuilder(SqlSessionFactoryBuilder sqlSessionFactoryBuilder) {
        super.setSqlSessionFactoryBuilder(sqlSessionFactoryBuilder); // 由于父类sqlSessionFactoryBuilder字段为私有，覆盖次注入方法后，必须给父类手动注入一次
        this.sqlSessionFactoryBuilder = sqlSessionFactoryBuilder;
    }

    /**
     * 覆盖父类，拦截直接注入的mapperLocations，在子类中解析，禁止在父类中解析。
     */
    @Override
    public void setMapperLocations(Resource[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public void setEnumBasePackages(String enumBasePackages) {
        this.enumBasePackages = enumBasePackages;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        SqlSessionFactory oldSqlSessionFactory = super.buildSqlSessionFactory();
        Configuration configuration = oldSqlSessionFactory.getConfiguration();
        // 注意：type handler的注册必须在mapperLocations解析之前
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
        String[] enumPackages = parseEnumBasePackage();
        if (null != enumPackages) {
            Set<Class<? extends IEnum>> enumClasses = doScanEnumClass(enumPackages);
            if (null != enumClasses) {
                for (Class<? extends IEnum> cls : enumClasses) {
                    registry.register(cls, getEnumValueTypeHandlerInstance(cls));// 显示注册枚举处理器
                    if (logger.isDebugEnabled()) {
                        logger.debug("EnumValueTypeHandler is registered for type " + cls.getName());
                    }
                }
            }
        }

        if (!isEmpty(this.mapperLocations)) {
            for (Resource mapperLocation : this.mapperLocations) {
                if (mapperLocation == null) {
                    continue;
                }

                try {
                    XMLMapperBuilder xmlMapperBuilder =
                            new XMLMapperBuilder(mapperLocation.getInputStream(), configuration, mapperLocation.toString(),
                                configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                } catch (Exception e) {
                    throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
                } finally {
                    ErrorContext.instance().reset();
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("Parsed mapper file: '" + mapperLocation + "'");
                }
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Property 'mapperLocations' was not specified or no matching resources found");
            }
        }
        return this.sqlSessionFactoryBuilder.build(configuration);
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
