package com.dong.jwt.config.jwt.token.properties;

public interface RefreshTokenProperties extends AccessTokenProperties{
   int EXPIRE_TIME = 60000* 28800; // 만료시간(1000 = 1초) : 현재시간 + 2주
    String HEADER_STRING = "RefreshToken";
}
