package com.dong.jwt.controller;

import com.dong.jwt.model.User;
import com.dong.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home(){
        return "<h1>home</h1>";
    }


    @PostMapping("/token")
    public String postToken(){
        System.out.println("토큰입장!");
        return "<h1>Token</h1>";
    }

    @PostMapping("/join")
    public String join(@RequestBody User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "<h1>good</h1>";
    }

    @GetMapping("api/v1/user")
    public String user(){
        return "user";
    }

    @GetMapping("api/v1/manager")
    public String manager(){
        return "manager";
    }

    @GetMapping("api/v1/admin")
    public String admin(){
        return "admin";
    }
}
