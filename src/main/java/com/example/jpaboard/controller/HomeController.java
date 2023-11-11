package com.example.jpaboard.controller;

import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.dto.CategoryDTO;
import com.example.jpaboard.entity.Category;
import com.example.jpaboard.service.BoardService;
import com.example.jpaboard.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CategoryService categoryService;
    private final BoardService boardService;

    @GetMapping("/")
    public String home(Model model){
        List<CategoryDTO> categories = categoryService.list();
        model.addAttribute("categories", categories);

        List<BoardDTO> boards = boardService.list();
        model.addAttribute("boards", boards);

        return "index";
    }}
