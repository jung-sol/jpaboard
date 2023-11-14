package com.example.jpaboard.dto;

import com.example.jpaboard.entity.User;
import com.example.jpaboard.entity.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateRequest {

    private Long id;

    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String passwordBefore;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    private String password;

    private String passwordCheck;

    private String name;

    private String nickname;

    public User toUser(String encodedPassword) {
        return User.builder()
                .id(this.id)
                .loginId(this.loginId)
                .password(encodedPassword)
                .nickname(this.nickname)
                .name(this.name)
                .role(UserRole.USER)
                .build();
    }

    public static UpdateRequest toReq(User user) {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setId(user.getId());
        updateRequest.setLoginId(user.getLoginId());
        updateRequest.setName(user.getName());
        updateRequest.setPassword(user.getPassword());
        updateRequest.setNickname(user.getNickname());

        return updateRequest;
    }
}
