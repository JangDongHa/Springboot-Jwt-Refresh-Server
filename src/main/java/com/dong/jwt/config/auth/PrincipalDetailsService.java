package com.dong.jwt.config.auth;

import com.dong.jwt.model.User;
import com.dong.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
