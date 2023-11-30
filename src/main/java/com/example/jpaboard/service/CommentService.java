package com.example.jpaboard.service;

import com.example.jpaboard.dto.CommentDTO;
import com.example.jpaboard.entity.Board;
import com.example.jpaboard.entity.Comment;
import com.example.jpaboard.repository.BoardRepository;
import com.example.jpaboard.repository.CommentCustomRepository;
import com.example.jpaboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final CommentCustomRepository commentCustomRepository;

    public void save(CommentDTO commentDTO) {
        Comment comment = Comment.builder()
                    .content(commentDTO.getContent())
                    .user(commentDTO.getUser())
                    .board(commentDTO.getBoard())
                    .parent(commentDTO.getParent())
                    .build();

        commentRepository.save(comment);
    }

    public List<CommentDTO> findByBoard(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);

        if (optionalBoard.isEmpty()) {
            throw new IllegalStateException("없는 게시물입니다.");
        }

        List<Comment> commentList = commentRepository.findByBoard(optionalBoard.get());
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentDTOList.add(CommentDTO.builder()
                            .id(comment.getId())
                            .user(comment.getUser())
                            .createTime(comment.getCreatedTime())
                            .updateTime(comment.getUpdatedTime())
                            .board(comment.getBoard())
                            .content(comment.getContent())
                            .parent(comment.getParent())
                            .build());
        }
        return commentDTOList;
    }

    public List<CommentDTO> findByBoardOrderBy(Long board_id) {
        List<Comment> commentList = commentCustomRepository.findAllByBoard(board_id);

        return convertNestedStructure(commentList);
    }

    private List<CommentDTO> convertNestedStructure(List<Comment> comments) {
        List<CommentDTO> result = new ArrayList<>();
        Map<Long, CommentDTO> map = new HashMap<>();
        comments.stream().forEach(c -> {
            CommentDTO dto = CommentDTO.toCommentDto(c);
            map.put(dto.getId(), dto);

            if(c.getParent() != null) {
                CommentDTO commentDTO = map.get(c.getParent().getId());
                List<CommentDTO> children = commentDTO.getChildren();
                children.add(dto);
            } else {
                result.add(dto);
            }
        });
        return result;
    }
}
