package com.dong.jwt.controller;

import com.dong.jwt.dto.JoinUserDto;
import com.dong.jwt.dto.ResponseDto;
import com.dong.jwt.model.User;
import com.dong.jwt.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "UserApiController")
@RestController
public class UserApiController {
    @Autowired
    private UserServiceImpl userService;



    @ApiOperation(value = "회원가입", notes = "닉네임은 `최소 4자 이상, 12자 이하 알파벳 대소문자(a~z, A~Z), 숫자(0~9)`로 구성 및 비밀번호는 `최소 4자 이상이며, 32자 이하 알파벳 소문자(a~z), 숫자(0~9)` 로 구성")
    @PostMapping("/api/user/join")
    public ResponseDto<String> join(
            @ApiParam(value = "회원가입 Form", required = true, example = "username, password, passwordCheck")
            @RequestBody JoinUserDto joinUserDto){
        if (joinUserDto.getPassword().equals(joinUserDto.getPasswordCheck())){
            User user = new User();
            user.setUsername(joinUserDto.getUsername());
            user.setPassword(joinUserDto.getPassword());
            return userService.joinUser(user);
        }
        return new ResponseDto<>(HttpStatus.OK, "비밀번호와 비밀번호 검사가 일치하지 않습니다.");
    }


}
