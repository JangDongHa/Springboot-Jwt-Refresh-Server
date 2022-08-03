package com.dong.jwt.service;

import com.dong.jwt.dto.BoardDto;
import com.dong.jwt.dto.RequestBoardUpdateDeleteCheckDto;
import com.dong.jwt.dto.BoardDetailDto;
import com.dong.jwt.dto.ResponseDto;
import com.dong.jwt.model.Board;
import com.dong.jwt.model.User;

import java.util.List;

public interface BoardService {
    boolean postBoard(BoardDto boardDto);
    BoardDetailDto getBoard(long id);
    ResponseDto<String> deleteBoard(long id, User user) throws Exception;
    ResponseDto<String> reviseBoard(BoardDto boardDto) throws Exception;
    boolean isSameUserInBoard(Board board, User user);
    List<BoardDetailDto> getAllBoardList();
    List<BoardDetailDto> makeBoardToResponseBoardDto(List<Board> boards);
}
