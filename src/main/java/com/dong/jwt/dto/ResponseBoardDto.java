package com.dong.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Builder
public class ResponseBoardDto {
    private String title;
    private String author;
    private String content;
    private Timestamp createTime;

    public ResponseBoardDto(String title, String author, String content, Timestamp createTime){
        this.title = title;
        this.author = author;
        this.content = content;
        this.createTime = createTime;
    }
}
