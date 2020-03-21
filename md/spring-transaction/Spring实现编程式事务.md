### 如何实现编程事务？

1. 第一步:注入transactionTemplate
```
@Autowired
private TransactionTemplate transactionTemplate;
```
2. 第二步:调用execute方法
 - 在doInTransactionWithoutResult中实现数据事务方法
 - transactionStatus.setRollbackOnly 表示只能回滚
```
transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
				jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'aaa')");
				log.info("COUNT IN TRANSACTION: {}", getCount());
				//设置只能回滚
				transactionStatus.setRollbackOnly();
			}
		});
```

