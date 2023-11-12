package com.example.jpaboard.controller;

import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.dto.CategoryDTO;
import com.example.jpaboard.entity.Category;
import com.example.jpaboard.entity.User;
import com.example.jpaboard.service.BoardService;
import com.example.jpaboard.service.CategoryService;
import com.example.jpaboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CategoryService categoryService;
    private final BoardService boardService;
    private final UserService userService;

    @GetMapping("/")
    public String home(Model model, Authentication auth){
        List<CategoryDTO> categories = categoryService.list();
        model.addAttribute("categories", categories);

        List<BoardDTO> boards = boardService.list();
        model.addAttribute("boards", boards);

        if (auth != null) {
            User loginUser = userService.getLoginUserByLoginId(auth.getName());

            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname());
            }
        }

        return "index";
    }}
