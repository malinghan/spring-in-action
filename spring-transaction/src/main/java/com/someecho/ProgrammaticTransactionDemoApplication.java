package com.someecho;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootApplication
@Slf4j
public class ProgrammaticTransactionDemoApplication implements CommandLineRunner {

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(ProgrammaticTransactionDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("COUNT BEFORE TRANSACTION: {}", getCount());
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
				jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'aaa')");
				log.info("COUNT IN TRANSACTION: {}", getCount());
				//设置只能回滚
//				transactionStatus.setRollbackOnly();
			}
		});
		log.info(transactionTemplate.getTransactionManager().toString());
		log.info("COUNT AFTER TRANSACTION: {}", getCount());
	}

//	@Override
//	public void run(String... args) throws Exception {
//		log.info("COUNT BEFORE TRANSACTION: {}", getCount());
//		transactionTemplate.execute(new TransactionCallback<String>() {
//
//			@Override
//			public String doInTransaction(TransactionStatus status) {
//				jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'aaa')");
//				log.info("COUNT IN TRANSACTION: {}", getCount());
//				status.hasSavepoint();
//				return "测试";
//			}
//		});
//		log.info("COUNT AFTER TRANSACTION: {}", getCount());
//	}

	private long getCount() {
		return (long) jdbcTemplate.queryForList("SELECT COUNT(*) AS CNT FROM FOO")
				.get(0).get("CNT");
	}
}

