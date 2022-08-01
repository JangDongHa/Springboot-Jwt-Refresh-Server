package com.dong.jwt.repository;

import com.dong.jwt.model.TokenValidate;
import com.dong.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenValidateRepository extends JpaRepository<TokenValidate, Long> {
    TokenValidate findByUser(User user);
    void deleteAllByUser(User user);
}
