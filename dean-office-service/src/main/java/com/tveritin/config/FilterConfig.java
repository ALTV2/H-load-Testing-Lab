package com.tveritin.config;

import com.tveritin.filter.PostRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<PostRequestFilter> postRequestFilterRegistration(PostRequestFilter filter) {
        FilterRegistrationBean<PostRequestFilter> registration = new FilterRegistrationBean<>(filter);
        registration.addUrlPatterns("/api/*");
        registration.setOrder(1);
        return registration;
    }
}