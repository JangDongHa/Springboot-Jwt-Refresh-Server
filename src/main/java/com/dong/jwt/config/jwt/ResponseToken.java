package com.dong.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dong.jwt.config.auth.PrincipalDetails;
import lombok.Getter;

import java.util.Date;

@Getter
public class ResponseToken {
    private String accessToken;
    private String refreshToken;
    private PrincipalDetails principalDetails;

    public ResponseToken(PrincipalDetails principalDetails){
        this.principalDetails = principalDetails;
        accessToken = makeToken("accessToken", AccessTokenProperties.EXPIRE_TIME);
        refreshToken = makeToken("refreshToken", RefreshTokenProperties.EXPIRE_TIME);
    }


    public String makeToken(String tokenName, int expire){
        return JWT.create()
                .withSubject(tokenName)
                .withExpiresAt(new Date(System.currentTimeMillis() + expire ))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(CommonTokenProperties.SECRET));
    }
}
