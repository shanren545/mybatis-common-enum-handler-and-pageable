mybatis-common-enum-handler-and-pageable
========================================

本项目实现mybatis中enum到int映射的通用处理机制，及扩展mybatis-generator 自动生成分页功能的插件

#通过EnumValueTypeHandler、IEnum、MybatisSqlSessionFactoryBean三个类实现enum与int在java与db之间的自动转换

#通过Page、PageInfo、PostgreSqlPagingPlugin、MysqlPagingPlugin实现分页功能自动生成，分页用法参见单元测试