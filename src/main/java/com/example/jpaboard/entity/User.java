package com.example.jpaboard.entity;

import com.example.jpaboard.dto.JoinRequest;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String password;

    private String name;

    private String nickname;

    private UserRole role;

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Heart> heartList = new ArrayList<>();

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }

}
