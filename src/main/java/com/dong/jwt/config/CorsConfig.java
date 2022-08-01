package com.dong.jwt.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 내 서버가 응답할 때 json을 자바스크립트를 처리할 수 있게 할 지를 설정 -> 쉽게 말해서 js와 통신할 수 있게 하기 위해서
        config.addAllowedOrigin("*"); // 모든 IP에 대한 응답을 허용
        config.addAllowedHeader("*"); // 모든 Header에 대한 응답을 허용
        config.addAllowedMethod("*"); // 모든 메소드(GET, POST, PUT....)에 대한 응답을 허용

        System.out.println("CORS Filter!");
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
