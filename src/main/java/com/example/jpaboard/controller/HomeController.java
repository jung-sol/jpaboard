package com.example.jpaboard.controller;

import com.example.jpaboard.auth.PrincipalDetailsService;
import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.dto.CategoryDTO;
import com.example.jpaboard.entity.Category;
import com.example.jpaboard.entity.User;
import com.example.jpaboard.entity.UserRole;
import com.example.jpaboard.service.BoardService;
import com.example.jpaboard.service.CategoryService;
import com.example.jpaboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CategoryService categoryService;
    private final BoardService boardService;
    private final UserService userService;

    @ModelAttribute("loginId")
    public String loginId(Authentication auth) {
        if (auth != null) {
            return auth.getName();
        }
        return null;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication auth) {
        if (auth != null) {
            return userService.checkAdmin(auth.getName());
        }
        return false;
    }

    @GetMapping("/")
    public String home(Model model, Authentication auth){
        List<CategoryDTO> categories = categoryService.list();
        model.addAttribute("categories", categories);

        List<BoardDTO> boards = boardService.list();
        model.addAttribute("boards", boards);

        /*
        if (auth != null) {
            model.addAttribute("loginId", auth.getName());

            if (userService.checkAdmin(auth.getName())) {
                model.addAttribute("isAdmin", true);
            }
        }

         */

        return "index";
    }}
