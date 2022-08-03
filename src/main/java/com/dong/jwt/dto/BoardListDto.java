package com.dong.jwt.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
public class BoardListDto {
    private String title;
    private String author;
    private Timestamp createTime;

    public BoardListDto(String title, String author, Timestamp createTime){
        this.title = title;
        this.author = author;
        this.createTime = createTime;
    }
}
