package com.dong.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestBoardUpdateDeleteCheckDto {
    private long id;
    private String password;
}
