package com.dong.jwt.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String roles; // USER

    public List<String> getRoleList(){
        if (this.roles.length() > 0){ // 존재하면
            return Arrays.asList(this.roles.split(",")); // 리스트로 뿌려주고
        }
        return new ArrayList<>(); // 존재하지 않으면 빈값
    }

}
