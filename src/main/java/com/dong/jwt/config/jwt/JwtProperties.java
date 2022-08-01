package com.dong.jwt.config.jwt;

public interface JwtProperties {
    String SECRET = "cos";
    int EXPIRE_TIME = 60000*10; // 만료시간(1000 = 1초) : 현재시간 + 10분
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
