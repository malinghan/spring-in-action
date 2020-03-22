## 参考项目:
https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter

### DruidDataSourceAutoConfigure
- DruidDataSource自动配置
### DruidDataSourceBuilder
- DruidDataSource构建者
### DruidDataSourceWrapper
- DruidDataSource扩展点
### SomeStatFilter 
配置sql拦截信息
- extends StatFilter

### SomeWebStatFilter
配置web拦截信息
- extends AbstractWebStatImpl implements Filter

### SomeStatInterceptor 
- implements MethodInterceptor, InitializingBean, DisposableBean
- 用于拦截请求，然后输出慢请求

### DruidStatProperties

### DruidFilterConfiguration
用于自动解析properties中的filter配置
- statFilter
- configFilter
- encodingConvertFilter
- slf4jLogFilter
- log4jFilter
- log4j2Filter
- commonsLogFilter
- wallConfig
- wallFilter

### DruidSpringAopConfiguration
配置spring.datasource.druid.aop-patterns
- advice
- advisor
- advisorAutoProxyCreator

### DruidStatViewServletConfiguration
配置监控界面信息
- statViewServletRegistrationBean

### DruidWebStatFilterConfiguration
配置监控界面过滤处理信息
- webStatFilterRegistrationBean
