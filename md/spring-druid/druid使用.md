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
- 用在循环获取连接时的策略
- DruidDataSource#getConnectionDirect
  - test-on-borrow 连接复用检测,如果不能使用，则丢弃
  - test-on-return 连接返回检测,如果不能使用，则丢弃
  - test-while-idle 连接闲置检测,,如果不能使用，则丢弃
```
spring.datasource.druid.test-on-borrow=true
spring.datasource.druid.test-on-return=true
spring.datasource.druid.test-while-idle=true
```
### filter的含义
```
  public DruidPooledConnection getConnection(long maxWaitMillis) throws SQLException {
        init();

        if (filters.size() > 0) {
            FilterChainImpl filterChain = new FilterChainImpl(this);
            return filterChain.dataSource_connect(this, maxWaitMillis);
        } else {
            return getConnectionDirect(maxWaitMillis);
        }
    }
```

### 注意事项

1. 没有必要不要在生产环境打开监控的servlet

2. 没有连接泄漏可能的情况下，不要开启removeAbandoned,影响性能

3. testXXX会在每次连接时触发

4. 务必配置合理的慢SQL时间