package com.example.jpaboard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String loginId;

    private String password;

    private String name;

    private String nickname;

    private UserRole role;

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();
}
