package com.example.jpaboard.dto;

import com.example.jpaboard.entity.User;
import com.example.jpaboard.entity.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdatePasswordRequest {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String passwordBefore;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    private String password;

    private String passwordCheck;

}
