## 如何实现Spring数据库相关配置

1. 配置数据源 `DataSource`  `DataSourceFactory`
2. 配置事务管理器 `PlatformTransactionManager` `DataSourceTransactionManager`
3. 配置JdbcTemplate(可选)
4. SqlSessionFactory `Mybatis需要`



- 配置Spring数据源
```
@Bean(destroyMethod = "close")
    public DataSource dataSource() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("driverClassName", "org.h2.Driver");
        properties.setProperty("url", "jdbc:h2:mem:testdb");
        properties.setProperty("username", "sa");
        return BasicDataSourceFactory.createDataSource(properties);
    }
```
- 配置事务管理器(可选)
 