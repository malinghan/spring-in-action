## 配置多数据源

第一步: 配置多数据源的属性
```
# 数据源foo
foo.datasource.url=jdbc:h2:mem:foo
foo.datasource.username=sa
foo.datasource.password=

数据源bar
bar.datasource.url=jdbc:h2:mem:bar
bar.datasource.username=sa
bar.datasource.password=
```

第二步: 去除AutoConfiguration
- 数据源
- 事务管理器
- JdbcTemplate
```
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class})
```
第二步: 引入属性配置
```
 @Bean
    @ConfigurationProperties("foo.datasource")
    public DataSourceProperties fooDataSourceProperties() {
        return new DataSourceProperties();
    }
    
 @Bean
    @ConfigurationProperties("bar.datasource")
    public DataSourceProperties barDataSourceProperties() {
        return new DataSourceProperties();
    }
```

第三步: 注入多个数据源

```
    @Bean
    public DataSource barDataSource() {
        DataSourceProperties dataSourceProperties = barDataSourceProperties();
        log.info("bar datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
     @Bean
     public DataSource fooDataSource() {
         DataSourceProperties dataSourceProperties = fooDataSourceProperties();
         log.info("foo datasource: {}", dataSourceProperties.getUrl());
         return dataSourceProperties.initializeDataSourceBuilder().build();
     }   
```

第四步: 注入事务管理器
```
@Bean
    @Resource
    public PlatformTransactionManager fooTxManager(DataSource fooDataSource) {
        return new DataSourceTransactionManager(fooDataSource);
    }
 @Bean
    @Resource
    public PlatformTransactionManager barTxManager(DataSource barDataSource) {
        return new DataSourceTransactionManager(barDataSource);
    }    
```

第五步: 使用
```
@Resource(name= "fooDataSource")
private DataSource dataSource
```