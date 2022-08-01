package com.dong.jwt.controller;

import com.dong.jwt.dto.BoardDto;
import com.dong.jwt.dto.RequestBoardPostDto;
import com.dong.jwt.dto.RequestBoardUpdateDeleteCheckDto;
import com.dong.jwt.dto.ResponseBoardDto;
import com.dong.jwt.service.impl.BoardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoardApiController {

    private final BoardServiceImpl boardService;

    @Autowired
    public BoardApiController(BoardServiceImpl boardService){
        this.boardService = boardService;
    }

    @GetMapping("/api/board")
    public List<ResponseBoardDto> getAllBoardList(){
        return boardService.getAllBoardList();
    }

    @GetMapping("/api/board/{id}")
    public ResponseBoardDto getBoardList(@PathVariable long id){
        return boardService.getBoard(id);
    }

    @PostMapping("/api/board")
    public boolean postBoard(@RequestBody RequestBoardPostDto requestBoardPostDto){
        return boardService.postBoard(requestBoardPostDto);
    }

    @PostMapping("/api/board/securityCheck")
    public boolean postCheckBoard(@RequestBody RequestBoardUpdateDeleteCheckDto requestBoardUpdateDeleteCheckDto){
        return boardService.checkPasswordBoard(requestBoardUpdateDeleteCheckDto);
    }


    @PutMapping("/api/board")
    public void updateBoard(@RequestBody BoardDto boardDto){
        boardService.reviseBoard(boardDto);
    }

    @DeleteMapping("/api/board")
    public void deleteBoard(@RequestBody RequestBoardUpdateDeleteCheckDto requestBoardUpdateDeleteCheckDto){
        boardService.deleteBoard(requestBoardUpdateDeleteCheckDto);
    }

}
