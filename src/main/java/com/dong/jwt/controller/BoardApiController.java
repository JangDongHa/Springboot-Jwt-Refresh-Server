package com.dong.jwt.controller;

import com.dong.jwt.config.auth.PrincipalDetails;
import com.dong.jwt.dto.*;
import com.dong.jwt.model.Comment;
import com.dong.jwt.service.impl.BoardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoardApiController {

    private final BoardServiceImpl boardService;

    @Autowired
    public BoardApiController(BoardServiceImpl boardService){
        this.boardService = boardService;
    }

    @GetMapping("/api/board") // 게시글 전체 조회
    public List<BoardDetailDto> getAllBoardList(){
        return boardService.getAllBoardList();
    }

    @GetMapping("/api/board/{id}") // 게시글 조회
    public BoardDetailDto getBoardList(@PathVariable long id){
        return boardService.getBoard(id);
    }

    @PostMapping("/api/board") // 게시글 작성
    public boolean postBoard(@RequestBody BoardDto boardDto, @AuthenticationPrincipal PrincipalDetails principalDetails){
        boardDto.setUser(principalDetails.getUser());
        return boardService.postBoard(boardDto);
    }


    @PutMapping("/api/board") // 게시글 수정
    public void updateBoard(@RequestBody BoardDto boardDto, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        boardDto.setUser(principalDetails.getUser());

        boardService.reviseBoard(boardDto);
    }

    @DeleteMapping("/api/board/{id}") // 게시글 삭제
    public ResponseDto<String> deleteBoard(@PathVariable long id, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        return boardService.deleteBoard(id, principalDetails.getUser());
    }

    @PostMapping("/api/board/{id}/comment") // 댓글 작성
    public ResponseDto<String> postComment(@PathVariable long id, @RequestBody CommentDto commentDto, @AuthenticationPrincipal PrincipalDetails principalDetails){
        commentDto.setUser(principalDetails.getUser());
        return boardService.postComment(id, commentDto);
    }

    @PutMapping("/api/board/{boardId}/comment/{commentId}")
    public ResponseDto<String> updateComment(@PathVariable long commentId, @RequestBody CommentDto commentDto, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        commentDto.setUser(principalDetails.getUser());
        return boardService.updateComment(commentId, commentDto);
    }

    @DeleteMapping("/api/board/{boardId}/comment/{commentId}")
    public ResponseDto<String> deleteComment(@PathVariable long commentId, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        return boardService.deleteComment(commentId, principalDetails.getUser());
    }

    @GetMapping("/api/board/{id}/comment") // 게시글 내 댓글 전체 조회
    public List<Comment> getCommentsInBoard(@PathVariable long id){
        return boardService.getAllCommentInboard(id);
    }



}
