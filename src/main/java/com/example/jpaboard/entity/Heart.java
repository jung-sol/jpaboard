package com.example.jpaboard.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Heart {

    @Id @Column(name = "heart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    /*
        연관관계 메서드
     */

    public void setBoard(Board board) {
        this.board = board;
        board.getHeartList().add(this);
    }

    public void setUser(User user) {
        this.user = user;
        user.getHeartList().add(this);
    }

}
