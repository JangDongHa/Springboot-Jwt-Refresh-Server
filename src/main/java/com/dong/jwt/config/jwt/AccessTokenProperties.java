package com.dong.jwt.config.jwt;

public interface AccessTokenProperties extends CommonTokenProperties{
    int EXPIRE_TIME = 60000*10; // 만료시간(1000 = 1초) : 현재시간 + 10분
    String HEADER_STRING = "Authorization";
}
