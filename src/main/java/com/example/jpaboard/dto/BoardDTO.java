package com.example.jpaboard.dto;

import com.example.jpaboard.entity.Board;
import com.example.jpaboard.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class BoardDTO {

    private Long id;

    private String title;

    private String content;

    private int boardHits;

    private Category category;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    public BoardDTO(Long id, String title, String content, int boardHits, Category category, LocalDateTime createdTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.boardHits = boardHits;
        this.category = category;
        this.createdTime = createdTime;
    }

    public static BoardDTO toBoardDTO(Board board) {
        BoardDTO boardDTO = new BoardDTO();

        boardDTO.setId(board.getId());
        boardDTO.setTitle(board.getTitle());
        boardDTO.setContent(board.getContent());
        boardDTO.setBoardHits(board.getBoardHits());
        boardDTO.setCategory(board.getCategory());
        boardDTO.setCreatedTime(board.getCreatedTime());
        boardDTO.setUpdatedTime(board.getUpdatedTime());

        return boardDTO;
    }

    @Override
    public String toString() {
        return "BoardDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", boardHits=" + boardHits +
                ", category=" + category +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
