package com.dong.jwt.service.impl;

import com.dong.jwt.dto.*;
import com.dong.jwt.model.Board;
import com.dong.jwt.model.Comment;
import com.dong.jwt.model.User;
import com.dong.jwt.repository.BoardRepository;
import com.dong.jwt.repository.CommentRepository;
import com.dong.jwt.repository.UserRepository;
import com.dong.jwt.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    private final BCryptPasswordEncoder encodePassword;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    public BoardServiceImpl(BoardRepository boardRepository, BCryptPasswordEncoder encodePassword, UserRepository userRepository, CommentRepository commentRepository){
        this.boardRepository = boardRepository;
        this.encodePassword = encodePassword;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }
    @Transactional
    @Override
    public boolean postBoard(BoardDto boardDto){
        Board board = Board.builder().title(boardDto.getTitle()).user(boardDto.getUser()).content(boardDto.getContent())
                .author(boardDto.getAuthor()).build();
        boardRepository.save(board);
        return true;
    }
    @Override
    public BoardDetailDto getBoard(long id){
        Board board = boardRepository.findById(id).orElseThrow(()->new IllegalArgumentException("ID를 찾을 수 없습니다."));
        BoardDetailDto boardDetailDto;
        boardDetailDto = BoardDetailDto.builder().title(board.getTitle()).author(board.getUser().getUsername())
                .content(board.getContent()).createTime(board.getCreateTime()).build();

        return boardDetailDto;
    }

    @Transactional
    @Override
    public ResponseDto<String> deleteBoard(long id, User user) throws Exception {
        Board board = boardRepository.findById(id).orElseThrow(()->new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 권한 체크
        if (board.getUser().getId() == user.getId()){
            boardRepository.deleteById(id);
        }
        else
            throw new Exception("삭제 권한이 없습니다.");

        return new ResponseDto<>(HttpStatus.OK, "게시글 삭제가 완료되었습니다.");
    }

    @Transactional
    @Override
    public ResponseDto<String> reviseBoard(BoardDto boardDto) throws Exception{
        Board board = boardRepository.findById(boardDto.getId()).orElseThrow(()->new IllegalArgumentException("ID를 찾을 수 없습니다."));

        if (isSameUserInBoard(board, boardDto.getUser())){
            board.setId(boardDto.getId());
            board.setUser(boardDto.getUser());
            board.setContent(boardDto.getContent());
            board.setAuthor(boardDto.getAuthor());
            board.setTitle(boardDto.getTitle());
        }
        else
            throw new Exception("수정 권한이 없습니다.");

        return new ResponseDto<>(HttpStatus.OK, "게시글 수정이 완료되었습니다.");
    }
    @Override
    public List<BoardDetailDto> getAllBoardList(){
        List<Board> boards = boardRepository.findAll();
        return makeBoardToResponseBoardDto(boards);
    }
    @Override
    public List<BoardDetailDto> makeBoardToResponseBoardDto(List<Board> boards){
        List<BoardDetailDto> boardDetailDtos = new ArrayList<>();

        for (int i = boards.size() - 1 ; i >= 0; i--){
            Board board = boards.get(i);
            BoardDetailDto boardDetailDto = new BoardDetailDto(board.getTitle(), board.getAuthor(), board.getContent(), board.getCreateTime());
            boardDetailDtos.add(boardDetailDto);
        }

        return boardDetailDtos;
    }

    @Override
    public boolean isSameUserInBoard(Board board, User user){
        return board.getUser().equals(user);
    }

    @Transactional
    public ResponseDto<String> postComment(long id, CommentDto commentDto){
        Board board = boardRepository.findById(id).orElseThrow(()->new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.update(commentDto.getUser(), commentDto.getComment(), board);

        commentRepository.save(comment);
        return new ResponseDto<>(HttpStatus.OK, "댓글 작성 완료");
    }

    @Transactional
    public ResponseDto<String> updateComment(long commentId, CommentDto commentDto) throws Exception {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new IllegalArgumentException("수정할 댓글을 찾을 수 없습니다."));

        // 권한 검사
        if (commentDto.getUser().getId() == comment.getUser().getId()){
            comment.setComment(commentDto.getComment());
            commentRepository.save(comment);
        }
        else
            throw new Exception("수정할 권한이 없습니다.");

        
        return new ResponseDto<>(HttpStatus.OK, "댓글 수정 완료");
    }

    @Transactional
    public ResponseDto<String> deleteComment(long commentId, User user) throws Exception {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new IllegalArgumentException("삭제할 댓글을 찾을 수 없습니다."));

        // 권한 검사
        if (user.getId() == comment.getUser().getId()){
            commentRepository.deleteById(commentId);
        }
        else
            throw new Exception("삭제할 권한이 없습니다.");

        return new ResponseDto<>(HttpStatus.OK, "댓글 삭제 완료");
    }

    @Transactional(readOnly = true)
    public List<Comment> getAllCommentInboard(long id){
        Board board = boardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        return board.getComments();
    }



}
