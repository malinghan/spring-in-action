package com.alibaba.druid.spring.boot.autoconfigure.stat;

/**
 * @author : linghan.ma
 * @Package com.alibaba.druid.spring.boot.autoconfigure.stat
 * @Description:
 * @date Date : 2020年03月22日 9:57 AM
 **/

import com.alibaba.druid.spring.boot.autoconfigure.interceptor.SomeStatInterceptor;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 *
 */
@ConditionalOnProperty("spring.datasource.druid.aop-patterns")
public class DruidSpringAopConfiguration {

    @Bean
    public Advice advice(DruidStatProperties properties) {
        return new SomeStatInterceptor(properties.getProjectName(),properties.getSlowServiceMillis());
    }

    @Bean
    public Advisor advisor(DruidStatProperties properties) {
        return new RegexpMethodPointcutAdvisor(properties.getAopPatterns(), advice(properties));
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
}