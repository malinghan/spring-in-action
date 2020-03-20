package com.someecho.spring.web.mvc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author : linghan.ma
 * @Package com.someecho.spring.web.mvc.config
 * @Description:
 * @date Date : 2020年03月15日 12:37 AM
 **/
@Configuration
@ComponentScan(basePackages = {"com.someecho.spring.web.mvc"})
public class AppConfig {
}
