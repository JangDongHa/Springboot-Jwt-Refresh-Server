package com.dong.jwt.dto;


import com.dong.jwt.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardDto {
    private Long id;
    private String title;
    private User user;
    private String author;
    private String content;
}
