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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public static Board toBoard(BoardDTO boardDTO) {
        Board board = new Board();

        board.setId(boardDTO.getId());
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setBoardHits(boardDTO.getBoardHits());
        board.setCategory(boardDTO.getCategory());
//        board.setUser(boardDTO.getUser());

        return board;
    }

    /*
        연관관계 메서드
     */
    public void setUser(User user) {
        this.user = user;
        user.getBoards().add(this);
    }

    public void updateBoard(Category category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }

}
