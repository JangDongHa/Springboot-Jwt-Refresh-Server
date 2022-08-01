package com.dong.jwt.config;

import com.dong.jwt.config.jwt.JwtAuthenticationFilter;
import com.dong.jwt.config.jwt.JwtAuthorizationFilter;
import com.dong.jwt.filter.MyFilter1;
import com.dong.jwt.filter.MyFilter3;
import com.dong.jwt.repository.TokenValidateRepository;
import com.dong.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;


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
                // 필터 순서가 조금 바뀐 것 같아 나중에 찾아봐야함
                .addFilterBefore(new MyFilter3(), LogoutFilter.class)// Security Filter 진행 도중 LogoutFilter 라는 필터가 실행되기 전에 실행된다는 것
                // 그냥 addFilter를 할 수 없음(SecurityFilterChain 형식의 필터만 addFilter 가능). 따라서 필터 발동 이전 혹은 이후에 대한 값을 설정할 수 있음
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않겠다는 정책
                .and()
                .apply(new MyCustomDsl()) // Controller에 @CrossOrigin 을 걸어서 필터를 등록할 수 있는데 이건 기본적으로 인증이 필요하지 않은 요청만 허용하는 것 (인증이 있는 경우 필터에 등록하는 과정이 필요)
                .and()
                .formLogin().disable() // JWT 로 로그인을 할 것이기 때문에 안함 (JWT 서버는 이 위까지는 기본)
                .httpBasic().disable() // 기본적인 http 로그인 방식을 사용하지 않겠다는 것 (세션 사용x + basic 사용 x 를 진행하면 Bearer 방식을 사용한다는 것)
                .authorizeRequests()
                    .antMatchers("/api").authenticated()//access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
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
