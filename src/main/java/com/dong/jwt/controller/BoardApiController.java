package com.dong.jwt.controller;

import com.dong.jwt.config.auth.PrincipalDetails;
import com.dong.jwt.dto.*;
import com.dong.jwt.model.Comment;
import com.dong.jwt.service.impl.BoardServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "BoardApiController")
@RestController
public class BoardApiController {
    private final String NEED_LOGIN = "로그인 필요 ";
    private final String NEED_AUTHORIZATION = "작성자 권한 필요 ";
    private final String NEED_TOKEN = "ACCESS TOKEN REFRESH TOKEN 필요 ";

    private final BoardServiceImpl boardService;


    @Autowired
    public BoardApiController(BoardServiceImpl boardService){
        this.boardService = boardService;
    }

    @ApiOperation(value = "게시글 전체 조회")
    @GetMapping("/api/board") // 게시글 전체 조회
    public List<BoardDetailDto> getAllBoardList(){
        return boardService.getAllBoardList();
    }

    @ApiOperation(value = "게시글 조회")
    @GetMapping("/api/board/{id}") // 게시글 조회
    public BoardDetailDto getBoardList(
            @ApiParam(value = "id", required = true, example = "1")
            @PathVariable long id
    ){
        return boardService.getBoard(id);
    }

    @ApiOperation(value = "게시글 작성", notes = NEED_LOGIN + NEED_TOKEN)
    @PostMapping("/api/board") // 게시글 작성
    public boolean postBoard(
            @ApiParam(value = "게시글 작성 내용", required = true, example = "title, author, content")
            @RequestBody BoardDto boardDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails){
        boardDto.setUser(principalDetails.getUser());
        return boardService.postBoard(boardDto);
    }


    @ApiOperation(value = "게시글 수정", notes = NEED_AUTHORIZATION + NEED_TOKEN)
    @PutMapping("/api/board") // 게시글 수정
    public void updateBoard(
            @ApiParam(value = "게시글 수정 내용", required = true, example = "title, author, content")
            @RequestBody BoardDto boardDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        boardDto.setUser(principalDetails.getUser());

        boardService.reviseBoard(boardDto);
    }

    @ApiOperation(value = "게시글 삭제", notes = NEED_AUTHORIZATION + NEED_TOKEN)
    @DeleteMapping("/api/board/{id}") // 게시글 삭제
    public ResponseDto<String> deleteBoard(
            @ApiParam(value = "삭제할 게시글 ID", required = true, example = "1")
            @PathVariable long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        return boardService.deleteBoard(id, principalDetails.getUser());
    }

    @ApiOperation(value = "댓글 작성", notes = NEED_LOGIN + NEED_TOKEN)
    @PostMapping("/api/board/{id}/comment") // 댓글 작성
    public ResponseDto<String> postComment(
            @ApiParam(value = "게시글 번호", required = true, example = "1")
            @PathVariable long id,
            @ApiParam(value = "댓글 작성 내용", required = true, example = "comment")
            @RequestBody CommentDto commentDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails){
        commentDto.setUser(principalDetails.getUser());
        return boardService.postComment(id, commentDto);
    }

    @ApiOperation(value = "댓글 수정", notes = NEED_AUTHORIZATION + NEED_TOKEN)
    @PutMapping("/api/board/{boardId}/comment/{commentId}")
    public ResponseDto<String> updateComment(
            @ApiParam(value = "댓글 수정 ID")
            @PathVariable long commentId,
            @ApiParam(value = "댓글 수정 내용", required = true, example = "comment")
            @RequestBody CommentDto commentDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        commentDto.setUser(principalDetails.getUser());
        return boardService.updateComment(commentId, commentDto);
    }

    @ApiOperation(value = "댓글 삭제", notes = NEED_AUTHORIZATION + NEED_TOKEN)
    @DeleteMapping("/api/board/{boardId}/comment/{commentId}")
    public ResponseDto<String> deleteComment(
            @ApiParam(value = "댓글 삭제 ID", required = true)
            @PathVariable long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        return boardService.deleteComment(commentId, principalDetails.getUser());
    }

    @ApiOperation(value = "게시글 내 댓글 전체 조회")
    @GetMapping("/api/board/{id}/comment") // 게시글 내 댓글 전체 조회
    public List<Comment> getCommentsInBoard(
            @ApiParam(value = "게시글 ID", required = true, example = "1")
            @PathVariable long id){
        return boardService.getAllCommentInboard(id);
    }



}
