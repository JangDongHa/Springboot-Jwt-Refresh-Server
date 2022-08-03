package com.dong.jwt.service.impl;

import com.dong.jwt.dto.ResponseDto;
import com.dong.jwt.model.User;
import com.dong.jwt.repository.UserRepository;
import com.dong.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public ResponseDto<String> joinUser(User user){
        if (userRepository.findByUsername(user.getUsername()) != null) {
            System.out.println(userRepository.findByUsername(user.getUsername()));
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }

        if (checkUsernameAndPassword(user.getUsername(), user.getPassword())){
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRoles("ROLE_USER");
            userRepository.save(user);

            return new ResponseDto<>(HttpStatus.OK, "회원가입 완료");
        }

        return new ResponseDto<>(HttpStatus.OK, "회원가입 실패 (닉네임 또는 비밀번호 구성이 잘못되었습니다.)");
    }

    private boolean checkUsernameAndPassword(String username, String password){
        final String usernameError = "닉네임은 최소 4자 이상, 12자 이하 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 구성되어야 합니다.";
        final String passwordError = "비밀번호는 최소 4자 이상이며, 32자 이하 알파벳 소문자(a~z), 숫자(0~9) 로 구성되어야 합니다.";
        if (!(username.length() >= 4 && username.length() <= 12 && !findStr("[^a-zA-Z0-9]", username))) // a~z, A~Z, 0~9 문자가 이외가 포함되면 false를 출력
            throw new IllegalArgumentException(usernameError);

        if (!(password.length() >= 4 && password.length() <= 32 && !findStr("[^a-z0-9]", password))) // a~z, 0~9 문자가 이외가 포함되면 false를 출력
            throw new IllegalArgumentException(passwordError);

        return true;
    }

    private boolean findStr(String regex, String str){
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(str);
        return m.find();
    }

}
