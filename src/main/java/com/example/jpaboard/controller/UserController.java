package com.example.jpaboard.controller;

import com.example.jpaboard.dto.JoinRequest;
import com.example.jpaboard.dto.LoginRequest;
import com.example.jpaboard.dto.UpdateNicknameRequest;
import com.example.jpaboard.dto.UpdateRequest;
import com.example.jpaboard.entity.User;
import com.example.jpaboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("joinRequest", new JoinRequest());

        return "user/join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model) {
        // id 중복 체크
        if (userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            System.out.println("id 중복");
            bindingResult.addError(new FieldError("joinRequest", "loginId", "이미 존재하는 아이디입니다."));
        }

        // 닉네임 중복 체크
        if (userService.checkNicknameDuplicate(joinRequest.getNickname())) {
            System.out.println("닉네임 중복");
            bindingResult.addError(new FieldError("joinRequest", "nickname", "이미 존재하는 닉네임입니다."));
        }

        // pw == pwch
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            System.out.println("pw 중복");
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return "user/join";
        }

        userService.join2(joinRequest);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "user/login";
    }

    @GetMapping("/info")
    public String infoForm(Model model, Authentication auth) {
        User user = userService.getLoginUserByLoginId(auth.getName());

        if (user == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("user", user);

        return "user/info";
    }

    @GetMapping("/updatePassword")
    public String updateForm(Model model, Authentication auth) {
        User user = userService.getLoginUserByLoginId(auth.getName());

        if (user == null) {
            return "redirect:/user/login";
        }

        UpdateRequest updateRequest = UpdateRequest.toReq(user);
        model.addAttribute("updateRequest", updateRequest);

        return "user/updatePassword";
    }

    @PostMapping("/updatePassword")
    public String update(@Valid @ModelAttribute UpdateRequest updateRequest, BindingResult bindingResult, Authentication auth, Model model) {
        User user = userService.getLoginUserByLoginId(auth.getName());

        // 비밀번호 일치 체크
        if (!userService.checkPassword(updateRequest.getPasswordBefore(), user.getPassword())) {
            bindingResult.addError(new FieldError("updateRequest", "passwordBefore", "틀린 비밀번호입니다."));
        }

        // 새 비밀번호 == 비밀번호 체크 일치 체크
        if (!updateRequest.getPassword().equals(updateRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("updateRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return "user/updatePassword";
        }

        userService.updatePassword(updateRequest);
        return "redirect:/user/info";

    }

    @GetMapping("/updateNickname")
    public String updateNickNameForm(Model model, Authentication auth) {
        User user = userService.getLoginUserByLoginId(auth.getName());
        UpdateNicknameRequest nicknameRequest = new UpdateNicknameRequest();
        nicknameRequest.setNickname(user.getNickname());
        model.addAttribute("nicknameRequest", nicknameRequest);

        return "/user/updateNickname";
    }

    @PostMapping("/updateNickname")
    public String updateNickname(@Valid @ModelAttribute UpdateNicknameRequest nicknameRequest, BindingResult bindingResult, Authentication auth, Model model) {

        System.out.println(userService.checkNicknameDuplicate(nicknameRequest.getNickname()));

        if (userService.checkNicknameDuplicate(nicknameRequest.getNickname())) {
            bindingResult.addError(new FieldError("nicknameRequest", "nickname", "이미 존재하는 닉네임입니다."));
        }

        if (bindingResult.hasErrors()) {
            return "user/updateNickname";
        }

        userService.updateNickname(auth.getName(), nicknameRequest.getNickname());

        return "redirect:/user/info";
    }
}
