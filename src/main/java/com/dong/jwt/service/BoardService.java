package com.dong.jwt.service;

import com.dong.jwt.dto.BoardDto;
import com.dong.jwt.dto.RequestBoardPostDto;
import com.dong.jwt.dto.RequestBoardUpdateDeleteCheckDto;
import com.dong.jwt.dto.ResponseBoardDto;
import com.dong.jwt.model.Board;

import java.util.List;

public interface BoardService {
    boolean postBoard(RequestBoardPostDto requestBoardPostDto);
    ResponseBoardDto getBoard(long id);
    void deleteBoard(RequestBoardUpdateDeleteCheckDto requestBoardUpdateDeleteCheckDto);
    void reviseBoard(BoardDto boardDto);
    boolean checkPasswordBoard(RequestBoardUpdateDeleteCheckDto requestBoardUpdateDeleteCheckDto);
    List<ResponseBoardDto> getAllBoardList();
    List<ResponseBoardDto> makeBoardToResponseBoardDto(List<Board> boards);
}
