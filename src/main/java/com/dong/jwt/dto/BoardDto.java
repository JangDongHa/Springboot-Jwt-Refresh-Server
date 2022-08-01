package com.dong.jwt.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private String password;
}
