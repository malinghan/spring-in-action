package com.alibaba.druid.spring.boot.autoconfigure.stat;

/**
 * @author : linghan.ma
 * @Package com.alibaba.druid.spring.boot.autoconfigure.stat
 * @Description:
 * @date Date : 2020年03月22日 9:58 AM
 **/

import com.alibaba.druid.spring.boot.autoconfigure.filter.SomeWebStatFilter;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 *
 */
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "spring.datasource.druid.web-stat-filter.enabled", havingValue = "true")
public class DruidWebStatFilterConfiguration {
    @Bean
    public FilterRegistrationBean webStatFilterRegistrationBean(DruidStatProperties properties) {
        DruidStatProperties.WebStatFilter config = properties.getWebStatFilter();
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        //WebStatFilter filter = new WebStatFilter();
        SomeWebStatFilter filter=new SomeWebStatFilter(properties.getProjectName(),properties.getSlowHttpMillis());
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(config.getUrlPattern() != null ? config.getUrlPattern() : "/*");
        registrationBean.addInitParameter("exclusions", config.getExclusions() != null ? config.getExclusions() : "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        if (config.getSessionStatEnable() != null) {
            registrationBean.addInitParameter("sessionStatEnable", config.getSessionStatEnable());
        }
        if (config.getSessionStatMaxCount() != null) {
            registrationBean.addInitParameter("sessionStatMaxCount", config.getSessionStatMaxCount());
        }
        if (config.getPrincipalSessionName() != null) {
            registrationBean.addInitParameter("principalSessionName", config.getPrincipalSessionName());
        }
        if (config.getPrincipalCookieName() != null) {
            registrationBean.addInitParameter("principalCookieName", config.getPrincipalCookieName());
        }
        if (config.getProfileEnable() != null) {
            registrationBean.addInitParameter("profileEnable", config.getProfileEnable());
        }
        return registrationBean;
    }
}
