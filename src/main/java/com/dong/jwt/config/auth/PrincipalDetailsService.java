package com.dong.jwt.config.auth;

import com.dong.jwt.model.User;
import com.dong.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 원래는 http://localhost:8080/login 에서 동작하지만 formLogin()이 disable 상태이므로 동작 x (필터로 따로 설정해줘야함)
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("/login");
        User user = userRepository.findByUsername(username);
        System.out.println("userEntity : " + user.toString());
        return new PrincipalDetails(user);
    }
}
