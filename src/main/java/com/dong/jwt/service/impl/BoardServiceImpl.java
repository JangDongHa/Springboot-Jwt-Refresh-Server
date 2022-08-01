package com.dong.jwt.service.impl;

import com.dong.jwt.dto.BoardDto;
import com.dong.jwt.dto.RequestBoardPostDto;
import com.dong.jwt.dto.RequestBoardUpdateDeleteCheckDto;
import com.dong.jwt.dto.ResponseBoardDto;
import com.dong.jwt.model.Board;
import com.dong.jwt.repository.BoardRepository;
import com.dong.jwt.service.BoardService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    private final BCryptPasswordEncoder encodePassword;

    public BoardServiceImpl(BoardRepository boardRepository, BCryptPasswordEncoder encodePassword){
        this.boardRepository = boardRepository;
        this.encodePassword = encodePassword;
    }
    @Override
    public boolean postBoard(RequestBoardPostDto requestBoardPostDto){
        String password = encodePassword.encode(requestBoardPostDto.getPassword());
        Board board = Board.builder().title(requestBoardPostDto.getTitle()).author(requestBoardPostDto.getAuthor()).content(requestBoardPostDto.getContent())
                .password(password).build();
        boardRepository.save(board);
        return true;
    }
    @Override
    public ResponseBoardDto getBoard(long id){

        Board board = boardRepository.findById(id).orElseThrow(()->new IllegalArgumentException("ID를 찾을 수 없습니다."));
        ResponseBoardDto responseBoardDto;
        responseBoardDto = ResponseBoardDto.builder().title(board.getTitle()).author(board.getAuthor()).content(board.getContent()).createTime(board.getCreateTime()).build();

        return responseBoardDto;
    }
    @Override
    public void deleteBoard(RequestBoardUpdateDeleteCheckDto requestBoardUpdateDeleteCheckDto){
        Board board = boardRepository.findById(requestBoardUpdateDeleteCheckDto.getId()).orElseThrow(()->new IllegalArgumentException("ID를 찾을 수 없습니다."));

        if (checkPasswordBoard(requestBoardUpdateDeleteCheckDto))
            boardRepository.delete(board);
    }
    @Override
    @Transactional
    public void reviseBoard(BoardDto boardDto){
        Board board = boardRepository.findById(boardDto.getId()).orElseThrow(()->new IllegalArgumentException("ID를 찾을 수 없습니다."));
        String password = encodePassword.encode(boardDto.getPassword());

        board.setId(boardDto.getId());
        board.setAuthor(boardDto.getAuthor());
        board.setContent(boardDto.getContent());
        board.setPassword(password);
        board.setTitle(boardDto.getTitle());
    }
    @Override
    public boolean checkPasswordBoard(RequestBoardUpdateDeleteCheckDto requestBoardUpdateDeleteCheckDto){
        String password = requestBoardUpdateDeleteCheckDto.getPassword();
        Board board = boardRepository.findById(requestBoardUpdateDeleteCheckDto.getId()).orElseThrow(()->new IllegalArgumentException("ID를 찾을 수 없습니다."));

        if (encodePassword.matches(password, board.getPassword()))
            return true;

        return false;
    }
    @Override
    public List<ResponseBoardDto> getAllBoardList(){
        List<Board> boards = boardRepository.findAll();
        return makeBoardToResponseBoardDto(boards);
    }
    @Override
    public List<ResponseBoardDto> makeBoardToResponseBoardDto(List<Board> boards){
        List<ResponseBoardDto> responseBoardDtos = new ArrayList<>();

        for (int i = boards.size() - 1 ; i >= 0; i--){
            Board board = boards.get(i); // 1. board(DB에 있던 객체) 하나 꺼내옴
            ResponseBoardDto responseBoardDto = new ResponseBoardDto(board.getTitle(), board.getAuthor(), board.getContent(), board.getCreateTime());
            responseBoardDtos.add(responseBoardDto);
        }

        return responseBoardDtos;
    }

    public ResponseBoardDto make(Board board){
        ResponseBoardDto responseBoardDto = new ResponseBoardDto(board.getTitle(), board.getAuthor(), board.getContent(), board.getCreateTime());
        return responseBoardDto;
    }

}
