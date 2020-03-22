### 第一步: 开启声明声明式事务注解

- [@EnableTransactionManagement的使用](https://www.cnblogs.com/zhuyeshen/p/10907632.html)
```
@EnableTransactionManagement(mode = AdviceMode.PROXY)
```

### 第二步: 在service方法上加上`@Transaction`

#### 指定事务的异常回滚策略
```
@Transactional(rollbackFor = RollbackException.class)
```

#### 指定事务的传播属性
 [事务的传播属性案例详解](https://www.jianshu.com/p/e1848e2aa7c3)
```
serviceA -> ServiceB
```
 
propagation,其中默认的是REQUIRED,常用的其实就3种，

- REQUIRED
```
Support a current transaction, create a new one if none exists. Analogous to EJB transaction attribute of the same name.
This is the default setting of a transaction annotation.
```
- SUPPORTS

```
Support a current transaction, execute non-transactionally if none exists. Analogous to EJB transaction attribute of the same name.
Note: For transaction managers with transaction synchronization, SUPPORTS is slightly different from no transaction at all, as it defines a transaction scope that synchronization will apply for. As a consequence, the same resources (JDBC Connection, Hibernate Session, etc) will be shared for the entire specified scope. Note that this depends on the actual synchronization configuration of the transaction manager.
```
- REQUEST_NEW
创建一个新的事务，如果当前已经有事务了，则将当前事务挂起
- NESTED(只适用于JDBC,不属于EJB规范)
```
Execute within a nested transaction if a current transaction exists, behave like REQUIRED otherwise. There is no analogous feature in EJB.
Note: Actual creation of a nested transaction will only work on specific transaction managers. Out of the box, this only applies to the JDBC DataSourceTransactionManager. Some JTA providers might support nested transactions as well.
```
```java
public enum Propagation {
     //REQUIRED: 如果事务不存在，则创建新的事务
	REQUIRED(TransactionDefinition.PROPAGATION_REQUIRED),
	SUPPORTS(TransactionDefinition.PROPAGATION_SUPPORTS),
	MANDATORY(TransactionDefinition.PROPAGATION_MANDATORY),
	REQUIRES_NEW(TransactionDefinition.PROPAGATION_REQUIRES_NEW),
	NOT_SUPPORTED(TransactionDefinition.PROPAGATION_NOT_SUPPORTED),
	NEVER(TransactionDefinition.PROPAGATION_NEVER),
	NESTED(TransactionDefinition.PROPAGATION_NESTED);
	}
```

#### 指定事务管理器(多个事务管理器的问题)

#### 指定隔离级别

#### 指定readOnly


### 声明式事务切面方式
Spring AOP
- [AOP PROXY与AspectJ的区别](https://www.jianshu.com/p/872d3dbdc2ca)