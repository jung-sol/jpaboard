package com.example.jpaboard.controller;

import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.dto.CategoryDTO;
import com.example.jpaboard.entity.User;
import com.example.jpaboard.service.BoardService;
import com.example.jpaboard.service.CategoryService;
import com.example.jpaboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    private final UserService userService;

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

    @GetMapping("/new")
    public String createForm(Model model, Authentication auth) {
        if (auth == null) {
            return "redirect:/user/login";
        }

        return "board/saveBoard";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute BoardDTO boardDTO, Authentication auth) {
        boardService.save(boardDTO, auth.getName());

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
    public String updateForm(@PathVariable Long id, Model model, Authentication auth) {
        BoardDTO board = boardService.findById(id);

        if (!board.getUser().getLoginId().equals(auth.getName())) {
            throw new IllegalStateException("잘못된 접근입니다.");
        }

        model.addAttribute("board", board);

        return "board/updateBoard";
    }

    @PostMapping("/{boardId}/update")
    public String update(@PathVariable Long boardId, @ModelAttribute BoardDTO boardDTO, Model model) {
        Long updateBoardId = boardService.update(
                boardId, boardDTO.getCategory(), boardDTO.getTitle(), boardDTO.getContent());

        BoardDTO updateBoard = boardService.findById(updateBoardId);

        model.addAttribute("board", updateBoard);

        return "board/detailBoard";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Authentication auth) {
        BoardDTO board = boardService.findById(id);

        if (!board.getUser().getLoginId().equals(auth.getName())) {
            throw new IllegalStateException("잘못된 접근입니다.");
        }

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

    @GetMapping("/user/{loginId}")
    public String findByLoginId(@PathVariable String loginId, Model model) {
        List<BoardDTO> boards = boardService.findByUserLoginId(loginId);
        model.addAttribute("boards", boards);
        model.addAttribute("pageName", loginId + "님이 작성한 글");

        return "index";
    }
}
