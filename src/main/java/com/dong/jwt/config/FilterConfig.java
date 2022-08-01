package com.dong.jwt.config;

import com.dong.jwt.filter.MyFilter1;
import com.dong.jwt.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    // 여기에 있는 FilterRegistrationBean 은 Security에서 진행하는 필터(Security FilterChain)가 다 끝나고 나서 실행
    @Bean
    public FilterRegistrationBean<MyFilter1> filter1(){
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*"); // 모든 URL 패턴은 전부 해당 필터를 거치게 함
        bean.setOrder(1); // 낮은 번호가 필터 중에서 가장 먼저 실행
        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2(){
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*"); // 모든 URL 패턴은 전부 해당 필터를 거치게 함
        bean.setOrder(0); // 낮은 번호가 필터 중에서 가장 먼저 실행
        return bean;
    }
}
