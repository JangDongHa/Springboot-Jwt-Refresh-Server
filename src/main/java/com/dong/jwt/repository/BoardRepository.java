package com.dong.jwt.repository;

import com.dong.jwt.model.Board;
import com.dong.jwt.model.Comment;
import com.dong.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
