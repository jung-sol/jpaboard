package com.example.jpaboard.dto;

import com.example.jpaboard.entity.Board;
import com.example.jpaboard.entity.User;
import com.example.jpaboard.entity.UserRole;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDTO {

    private Long id;

    private String loginId;

    private String name;

    private String nickname;

    private UserRole role;

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setLoginId(user.getLoginId());
        userDTO.setName(user.getName());
        userDTO.setNickname(user.getNickname());
        userDTO.setRole(user.getRole());

        return userDTO;
    }

}
