package com.dong.jwt.dto;

import com.dong.jwt.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestBoardUpdateDeleteCheckDto {
    private long id;
    private User user;
}
