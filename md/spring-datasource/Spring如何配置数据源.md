## 配置单数据源
1.第一步: 配置spring.datasource默认属性
```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.hikari.maximumPoolSize=5
spring.datasource.hikari.minimumIdle=5
spring.datasource.hikari.idleTimeout=600000
spring.datasource.hikari.connectionTimeout=30000
spring.datasource.hikari.maxLifetime=1800000
```

2.第二步: 注入DataSource
```
@Autowired
private DataSource dataSource;
```
此时DataSourceAutoConfiguration就会自动将属性传给dataSource

可以通过 actuator/beans验证

## 配置多数据源

```
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class})
```