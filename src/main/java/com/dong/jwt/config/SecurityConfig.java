package com.dong.jwt.config;

import com.dong.jwt.config.jwt.JwtAuthenticationFilter;
import com.dong.jwt.config.jwt.JwtAuthorizationFilter;
import com.dong.jwt.repository.TokenValidateRepository;
import com.dong.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CorsConfig config;


    @Autowired
    private TokenValidateRepository tokenValidateRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않겠다는 정책
                .and()
                .apply(new MyCustomDsl())
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/api/board/**").permitAll()
                    .antMatchers("/api/board/**").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/api/board/**").authenticated()
                    .antMatchers(HttpMethod.PUT,"/api/board/**").authenticated()
                    .anyRequest().permitAll()
                .and().build();
                
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(config.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager, tokenValidateRepository))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository, tokenValidateRepository));
        }
    }
}
