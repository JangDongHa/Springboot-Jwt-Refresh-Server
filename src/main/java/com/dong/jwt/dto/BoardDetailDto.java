package com.dong.jwt.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Builder
public class BoardDetailDto {
    private String title;
    private String author;
    private String content;
    private Timestamp createTime;

    public BoardDetailDto(String title, String author, String content, Timestamp createTime){
        this.title = title;
        this.author = author;
        this.content = content;
        this.createTime = createTime;
    }
}
