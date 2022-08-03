package com.dong.jwt.config.jwt;

import com.dong.jwt.config.auth.PrincipalDetails;
import com.dong.jwt.config.jwt.token.ResponseToken;
import com.dong.jwt.model.TokenValidate;
import com.dong.jwt.model.User;
import com.dong.jwt.repository.TokenValidateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    private final AuthenticationManager authenticationManager;

    private final TokenValidateRepository tokenValidateRepository;



    // /login 요청을 하면 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {

            ObjectMapper om = new ObjectMapper(); // JSON Data를 파싱해줌
            User user = om.readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()); // 토큰 생성

            Authentication authentication = authenticationManager.authenticate(token);


            // 로그인이 정상적으로 처리되면 authentication getPrincipal 에서 유저정보를 가져올 수 있음
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


            return authentication;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        ResponseToken responseToken = new ResponseToken(principalDetails);
        // Access Token 과 refresh Token 생성
        String accessToken = responseToken.getAccessToken();
        String refreshToken = responseToken.getRefreshToken();

        // 이후 DB에 삽입
        TokenValidate tokenDB = TokenValidate.builder().access_token(accessToken).refresh_token(refreshToken).user(principalDetails.getUser()).build();
        tokenValidateRepository.save(tokenDB);

        // 헤더에 삽입
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("RefreshToken", "Bearer " + refreshToken);




    }
}
