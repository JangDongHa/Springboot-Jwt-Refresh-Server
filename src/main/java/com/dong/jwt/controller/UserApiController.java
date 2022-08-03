package com.dong.jwt.controller;

import com.dong.jwt.dto.ResponseDto;
import com.dong.jwt.model.User;
import com.dong.jwt.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {
    @Autowired
    private UserServiceImpl userService;


    @PostMapping("/api/user/join")
    public ResponseDto<String> join(@RequestBody User user){
        return userService.joinUser(user);
    }


}
