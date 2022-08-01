package com.dong.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dong.jwt.config.auth.PrincipalDetails;
import com.dong.jwt.model.TokenValidate;
import com.dong.jwt.model.User;
import com.dong.jwt.repository.TokenValidateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
import java.util.Date;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 존재
// 원래 /login 해서 username, password 를 전송하면 UsernamePasswordAuthenticationFilter 가 동작함
// UsernamePasswordAuthenticationFilter 는 기본적으로 Username 과 Password 를 받고 어떤 처리 이후에 AuthenticationManager 에게 던져줘야함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    private final AuthenticationManager authenticationManager;

    private final TokenValidateRepository tokenValidateRepository;



    // /login 요청을 하면 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도!!");

        // 1. username 과 password 를 받아서
        try {
//            BufferedReader bufferedReader = request.getReader();
//
//            String input = null;
//            String inputData = "";
//            while((input = bufferedReader.readLine()) != null){
//                inputData += input;
//            }

            ObjectMapper om = new ObjectMapper(); // JSON Data를 파싱해줌
            User user = om.readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()); // 토큰 생성
            
            // PrincipalDetailsService의 loadUserByUsername() 이 실행된 후 정상이면 authentication 이 리턴되고 정상이 아니면 unAuthorized 가 throw 되면서 이 아래 있는 변수들은 실행 안됨
            Authentication authentication = authenticationManager.authenticate(token);
            
            // 로그인이 정상적으로 처리되면 authentication getPrincipal 에서 유저정보를 가져올 수 있음
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인완료 : " + principalDetails.getUser().getUsername());

            // return 의 이유는 권한관리를 security 가 대신 해주기 떄문에 진행
            // 사실 굳이 JWT 토큰을 사용하면서 세션을 만들 이유는 없음

            // 여기까지 하였으면 이제는 마지막으로 JWT Token 을 만들어주면 끝
            return authentication; // Authentication 객체가 Session 영역에 저장됨 => 로그인이 되었다는 뜻이므로 리턴해줌으로써 session 영역에 저장을 진행

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 2. 정상인지 로그인 시도를 해보는 것. authenticationManager 로 로그인 시도를 하면 PrincipalDetailsService 가 호출됨
        // 3. PrincipalDetails 를 세션에 담고
        // -> 세션에 담는 이유 : 세션에 담겨있어야 시큐리티의 hasRole ... 에 대한 access 검사를 통과할 수 있기 때문에 = 권한 관리를 위해서
        // 4. 마지막으로 JWT 토큰을 만들어서 응답해주면 끝

    }

    // 위의 attemptAuthentication 이 종료될 때 바로 다음 실행되는 메서드
    // 즉, 인증이 완료된 이후의 처리를 담당함
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

        System.out.println("완료");



    }
}
