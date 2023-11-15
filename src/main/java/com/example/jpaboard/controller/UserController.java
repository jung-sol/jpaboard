package com.example.jpaboard.controller;

import com.example.jpaboard.dto.*;
import com.example.jpaboard.entity.User;
import com.example.jpaboard.service.CategoryService;
import com.example.jpaboard.service.UserService;
import lombok.RequiredArgsConstructor;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final CategoryService categoryService;

    @ModelAttribute("categories")
    public List<CategoryDTO> categories() {
        List<CategoryDTO> categories = categoryService.list();

        return categories;
    }

    @ModelAttribute("loginId")
    public String loginId(Authentication auth) {
        if (auth != null) {
            return auth.getName();
        }
        return null;
    }

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
    public String updatePasswordForm(Authentication auth, Model model) {
        User user = userService.getLoginUserByLoginId(auth.getName());

        if (user == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("updatePasswordRequest", new UpdatePasswordRequest());

        return "user/updatePassword";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@Valid @ModelAttribute UpdatePasswordRequest updatePasswordRequest, BindingResult bindingResult, Authentication auth, Model model) {
        User user = userService.getLoginUserByLoginId(auth.getName());

        // 비밀번호 일치 체크
        if (!userService.checkPassword(updatePasswordRequest.getPasswordBefore(), user.getPassword())) {
            bindingResult.addError(new FieldError("updatePasswordRequest", "passwordBefore", "틀린 비밀번호입니다."));
        }

        // 새 비밀번호 == 비밀번호 체크 일치 체크
        if (!updatePasswordRequest.getPassword().equals(updatePasswordRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("updatePasswordRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return "user/updatePassword";
        }

        userService.updatePassword(auth.getName(), updatePasswordRequest.getPassword());

        return "redirect:/user/info";

    }

    @GetMapping("/updateNickname")
    public String updateNickNameForm(Model model, Authentication auth) {
        User user = userService.getLoginUserByLoginId(auth.getName());
        UpdateNicknameRequest updateNicknameRequest = new UpdateNicknameRequest();
        updateNicknameRequest.setNickname(user.getNickname());
        model.addAttribute("updateNicknameRequest", updateNicknameRequest);

        return "/user/updateNickname";
    }

    @PostMapping("/updateNickname")
    public String updateNickname(@Valid @ModelAttribute UpdateNicknameRequest updateNicknameRequest, BindingResult bindingResult, Model model, Authentication auth) {

        if (userService.checkNicknameDuplicate(updateNicknameRequest.getNickname())) {
            bindingResult.addError(new FieldError("updateNicknameRequest", "nickname", "이미 존재하는 닉네임입니다."));
        }

        if (bindingResult.hasErrors()) {
            return "user/updateNickname";
        }

        userService.updateNickname(auth.getName(), updateNicknameRequest.getNickname());

        return "redirect:/user/info";
    }
}
