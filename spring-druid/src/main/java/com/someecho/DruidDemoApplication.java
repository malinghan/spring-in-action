package com.someecho;

import com.someecho.service.FooService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootApplication
@Slf4j
public class DruidDemoApplication implements CommandLineRunner {

	@Autowired
	private DataSource dataSource;

	@Resource
	private FooService fooService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(DruidDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info(dataSource.toString());
		new Thread(() -> fooService.queryForUpdate()).start();
		new Thread(() -> fooService.queryForUpdate()).start();
		}
}

