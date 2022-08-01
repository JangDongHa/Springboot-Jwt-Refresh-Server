package com.dong.jwt.config.jwt;


import com.auth0.jwt.JWT;
import com.dong.jwt.config.auth.PrincipalDetails;
import com.dong.jwt.config.jwt.token.RequestToken;
import com.dong.jwt.config.jwt.token.ResponseToken;
import com.dong.jwt.config.jwt.token.properties.AccessTokenProperties;
import com.dong.jwt.config.jwt.token.properties.CommonTokenProperties;
import com.dong.jwt.model.TokenValidate;
import com.dong.jwt.model.User;
import com.dong.jwt.repository.TokenValidateRepository;
import com.dong.jwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 시큐리티가 filter 를 가지고 있는데 그 filter 중에 BasicAuthenticationFilter 라는 것이 있음
// 무조건 해당 filter 를 타긴 하는데 이 filter 가 하는 일은 기본적으로 토큰이 있는지(현재 클래스 기준) 확인하고 그에 따른 처리를 진행하는 것

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;
    private TokenValidateRepository tokenValidateRepository;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, TokenValidateRepository tokenValidateRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.tokenValidateRepository = tokenValidateRepository;
    }

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("인증이나 권한이 필요한 주소 요청이 됨");
        String jwtHeader = request.getHeader(AccessTokenProperties.HEADER_STRING);
        System.out.println("jwtHeader : " + jwtHeader);
        
        // JWT Token 을 검증하여 정상적인 사용자인지 확인해봐야함
        if (jwtHeader == null || !jwtHeader.startsWith(CommonTokenProperties.TOKEN_PREFIX)){ // 헤더가 없거나 Bearer 이 아닌 경우
            chain.doFilter(request, response);
            return;
        }

        RequestToken requestToken = new RequestToken(request);
        String usernameInAccessToken = requestToken.getTokenElement(requestToken.getAccessToken(), "username");
        String usernameInRefreshToken = requestToken.getTokenElement(requestToken.getRefreshToken(), "username");

        String expire = JWT.decode(requestToken.getAccessToken()).getExpiresAt().toString();
        System.out.println(expire);
        TokenValidate tokenValidate = tokenValidateRepository.findByUser(userRepository.findByUsername(usernameInRefreshToken));

        // 서명이 정상이라고 판단한 경우
        if (tokenValidate.getAccess_token().equals(requestToken.getAccessToken()) && tokenValidate.getRefresh_token().equals(requestToken.getRefreshToken())){
            // 세션에 등록
            User userEntity = userRepository.findByUsername(usernameInAccessToken);
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            Authentication authentication = // 매개변수 : UserDetails, 비밀번호, ROLE
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities()); // 임의로 인증완료된 객체를 생성

            SecurityContextHolder.getContext().setAuthentication(authentication); // 시큐리티를 저장할 수 있는 세션 공간 -> 이곳에 Authentication(인증) 강제 성공 처리

            ResponseToken responseToken = new ResponseToken(principalDetails);
            String accessToken = responseToken.getAccessToken();
            String refreshToken = responseToken.getRefreshToken();

            // 이후 DB에 삽입 (이전에 있던 JWT 를 수정하고)
            TokenValidate tokenDB = tokenValidateRepository.findByUser(userEntity);
            tokenDB.setAccess_token(accessToken);
            tokenDB.setRefresh_token(refreshToken);
            tokenValidateRepository.save(tokenDB);


            // 헤더에 삽입
            response.addHeader("Authorization", "Bearer " + accessToken);
            response.addHeader("RefreshToken", "Bearer " + refreshToken);

        }
        else{
            chain.doFilter(request, response);
            return;
        }



    }
}
