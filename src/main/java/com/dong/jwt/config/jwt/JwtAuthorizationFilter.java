package com.dong.jwt.config.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dong.jwt.config.auth.PrincipalDetails;
import com.dong.jwt.model.User;
import com.dong.jwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

// 시큐리티가 filter 를 가지고 있는데 그 filter 중에 BasicAuthenticationFilter 라는 것이 있음
// 무조건 해당 filter 를 타긴 하는데 이 filter 가 하는 일은 기본적으로 토큰이 있는지(현재 클래스 기준) 확인하고 그에 따른 처리를 진행하는 것
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // super 적혀있는거 지우고 작성해야함 (안그러면 filter 를 2번 탐)
        System.out.println("인증이나 권한이 필요한 주소 요청이 됨");
        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader : " + jwtHeader);
        
        // JWT Token 을 검증하여 정상적인 사용자인지 확인해봐야함
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")){ // 헤더가 없거나 Bearer 이 아닌 경우
            chain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String username = JWT
                .require(Algorithm.HMAC512("cos"))
                .build().verify(token)
                .getClaim("username").asString(); // 서명이 정상적으로 되었다는 건 정상처리 된 것 (여기가 검증과정인데 JWT 라이브러리가 알아서 정리해서 비교해줌)

        // 서명이 정상이라고 판단한 경우
        if (username != null){
            User userEntity = userRepository.findByUsername(username);
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            Authentication authentication = // 매개변수 : UserDetails, 비밀번호, ROLE
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities()); // 임의로 인증완료된 객체를 생성

            SecurityContextHolder.getContext().setAuthentication(authentication); // 시큐리티를 저장할 수 있는 세션 공간 -> 이곳에 Authentication(인증) 강제 성공 처리
            System.out.println("권한 : " + SecurityContextHolder.getContext().getAuthentication());
        }



    }
}
