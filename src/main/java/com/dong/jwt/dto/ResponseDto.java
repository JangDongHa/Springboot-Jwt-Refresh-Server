package com.dong.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ResponseDto<T> {
    HttpStatus httpStatus;
    T data;
}
