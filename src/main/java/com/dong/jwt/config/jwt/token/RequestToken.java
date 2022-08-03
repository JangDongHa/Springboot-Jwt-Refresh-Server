package com.dong.jwt.config.jwt.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dong.jwt.config.jwt.token.properties.AccessTokenProperties;
import com.dong.jwt.config.jwt.token.properties.CommonTokenProperties;
import com.dong.jwt.config.jwt.token.properties.RefreshTokenProperties;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class RequestToken { // Request 토큰이 왔을 때 토큰을 저장하고 토큰에 있는 값을 추출해줌
    private String accessToken;
    private String refreshToken;
    private HttpServletRequest request;

    private final String TOKEN_NOT_VERIFIED = "TOKEN_NOT_VERIFIED";

    public RequestToken(HttpServletRequest request){
        this.request = request;
        this.accessToken = request.getHeader(AccessTokenProperties.HEADER_STRING).replace(CommonTokenProperties.TOKEN_PREFIX, "");
        this.refreshToken = request.getHeader(RefreshTokenProperties.HEADER_STRING).replace(CommonTokenProperties.TOKEN_PREFIX, "");
    }

    public String getTokenElement(String token, String element){
        try{
            return JWT
                    .require(Algorithm.HMAC512(CommonTokenProperties.SECRET))
                    .build().verify(token)
                    .getClaim(element).asString(); // 서명이 정상적으로 되었다는 건 정상처리 된 것 (여기가 검증과정인데 JWT 라이브러리가 알아서 정리해서 비교해줌)
        }
        catch (TokenExpiredException e){// AccessToken이 만료된 경우
            return null;
        }
        catch (SignatureVerificationException e){ // 서명이 잘못된 경우
            return TOKEN_NOT_VERIFIED;
        }

    }



}
