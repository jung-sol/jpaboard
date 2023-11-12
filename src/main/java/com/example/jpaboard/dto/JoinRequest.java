package com.example.jpaboard.dto;

import com.example.jpaboard.entity.User;
import com.example.jpaboard.entity.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class JoinRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    private String passwordCheck;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    public User toUser() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .name(this.name)
                .role(UserRole.USER)
                .build();
    }

    public User toUser(String encodedPassword) {
        return User.builder()
                .loginId(this.loginId)
                .password(encodedPassword)
                .nickname(this.nickname)
                .name(this.name)
                .role(UserRole.USER)
                .build();
    }
}
