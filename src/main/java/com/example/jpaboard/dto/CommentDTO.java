package com.example.jpaboard.dto;

import com.example.jpaboard.entity.Board;
import com.example.jpaboard.entity.Comment;
import com.example.jpaboard.entity.User;
import com.example.jpaboard.entity.UserRole;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

    private Long id;

    private String content;

    private User user;

    private Board board;

    private Comment parent;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<CommentDTO> children = new ArrayList<>();

    public CommentDTO(Long id, String content, User user, Board board, Comment parent, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.board = board;
        this.parent = parent;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public static CommentDTO toCommentDto(Comment comment) {
        return new CommentDTO(
                comment.getId(), comment.getContent(), comment.getUser(), comment.getBoard(), comment.getParent(), comment.getCreatedTime(), comment.getUpdatedTime());
    }

}
