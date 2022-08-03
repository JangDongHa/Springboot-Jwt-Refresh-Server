package com.dong.jwt.service;

import com.dong.jwt.dto.ResponseDto;
import com.dong.jwt.model.User;

public interface UserService {
    ResponseDto<String> joinUser(User user);
}
