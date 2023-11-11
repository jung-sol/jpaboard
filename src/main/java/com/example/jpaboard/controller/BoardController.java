package com.example.jpaboard.controller;

import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.dto.CategoryDTO;
import com.example.jpaboard.service.BoardService;
import com.example.jpaboard.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final CategoryService categoryService;
    private final BoardService boardService;

    @ModelAttribute("categories")
    public List<CategoryDTO> categories() {
        List<CategoryDTO> categories = categoryService.list();

        return categories;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        return "board/saveBoard";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute BoardDTO boardDTO) {
        boardService.save(boardDTO);

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        boardService.updateHits(id);
        BoardDTO board = boardService.findById(id);
        model.addAttribute("board", board);

        return "board/detailBoard";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO board = boardService.findById(id);
        model.addAttribute("board", board);

        return "board/updateBoard";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        System.out.println(boardDTO);
        BoardDTO updateDTO = boardService.update(boardDTO);
        model.addAttribute("board", updateDTO);

        return "board/detailBoard";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);

        return "redirect:/";
    }

    @GetMapping("/category/{id}")
    public String findByCategoryId(@PathVariable Long id, Model model) {
        List<BoardDTO> boards = boardService.findByCategoryId(id);
        model.addAttribute("boards", boards);
        model.addAttribute("currentCategoryId", id);

        return "index";
    }
}
