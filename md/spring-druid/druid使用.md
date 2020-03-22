### 慢SQL监控配置
- Springboot
```
spring.datasource.druid.filter.stat.log-slow-sql= false
spring.datasource.druid.filter.stat.slow-sql-millis=3000
spring.datasource.druid.filter.stat.merge-sql=true
spring.datasource.druid.filter.stat.db-type=mysql
spring.datasource.druid.filter.stat.enabled=true
```

- spring
```
druid.stat.logSlowSql-true
druid.stat.slowSqlMills=3000
```
### testXXX的含义

- DruidDataSource#getConnectionDirect
```
spring.datasource.druid.test-on-borrow=true
spring.datasource.druid.test-on-return=true
spring.datasource.druid.test-while-idle=true
```

### 注意事项

1. 没有必要不要在生产环境打开监控的servlet

2. 没有连接泄漏可能的情况下，不要开启removeAbandoned,影响性能

3. testXXX会在每次连接时触发

4. 务必配置合理的慢SQL时间