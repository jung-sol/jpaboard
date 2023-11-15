package com.example.jpaboard.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class UpdateNicknameRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

}
