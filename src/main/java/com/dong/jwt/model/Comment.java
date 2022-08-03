package com.dong.jwt.model;

import com.dong.jwt.model.common.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    private String comment;

    @ManyToOne
    @JsonIgnore // 어차피 참조할 때 BoardId로 참조해서 Comment 를 빼므로 Board 역시 출력할 필요없음
    @JoinColumn(name = "board_id")
    private Board board;

    public void update(User user, String comment, Board board){
        this.user = user;
        this.comment = comment;
        this.board = board;
    }
}
