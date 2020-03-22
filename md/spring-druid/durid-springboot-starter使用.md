## druid-springboot-starter
### 简介
- 配置慢sql标准和输出格式
- 配置满请求标准和输出格式
- 配置监控界面信息
- DruidDataSource自动配置
- 创建数据源，同时加入自定义的数据源监控的扩展点

### 使用
提前:重写官方的druid-springboot-starter

- 加入自定义的慢sql拦截规范
- 加入自定义的接口拦截规范
- 加入自定义的监控界面输出规范
- 加入自定义的wallFilter

第一步:引入pom
```xml
<dependency>
        <groupId>com.xrxs</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.1.18-SNAPSHOT</version>
      </dependency>
```

第二步:创建数据源
```
  @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource()
    {
        return DruidDataSourceBuilder.create().build();
    }
```
第三步:属性配置

