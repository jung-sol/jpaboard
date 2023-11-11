package com.example.jpaboard.entity;

import com.example.jpaboard.dto.BoardDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Board extends BaseEntity{

    @Id @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    private int boardHits;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public static Board toBoard(BoardDTO boardDTO) {
        Board board = new Board();

        board.setId(boardDTO.getId());
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setBoardHits(boardDTO.getBoardHits());
        board.setCategory(boardDTO.getCategory());

        return board;
    }

}
