package com.example.jpaboard.controller;

import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.dto.CategoryDTO;
import com.example.jpaboard.entity.Heart;
import com.example.jpaboard.entity.User;
import com.example.jpaboard.entity.UserRole;
import com.example.jpaboard.service.BoardService;
import com.example.jpaboard.service.CategoryService;
import com.example.jpaboard.service.HeartService;
import com.example.jpaboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final CategoryService categoryService;
    private final BoardService boardService;
    private final UserService userService;
    private final HeartService heartService;

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

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication auth) {
        if (auth != null) {
            return userService.checkAdmin(auth.getName());
        }
        return false;
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
    public String findById(@PathVariable Long id, Model model, Authentication auth) {
        boardService.updateHits(id);
        BoardDTO board = boardService.findById(id);
        model.addAttribute("board", board);

        if (auth != null) {
            User user = userService.getLoginUserByLoginId(auth.getName());
            Long heart_id = heartService.checkStatusLike(id, user.getId());
            model.addAttribute("heartState", heart_id);
        }

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

        // 관리자 여부 확인
        boolean isAdmin = userService.checkAdmin(auth.getName());
        // 작성자 확인
        boolean isWriter = board.getUser().getLoginId().equals(auth.getName());

        // 작성자 & 관리자 삭제 가능
        if (!isAdmin && !isWriter) {
            throw new IllegalStateException("잘못된 접근입니다.");
        }

        boardService.delete(id);

        return "redirect:/";
    }

    @GetMapping("/heart/{id}")
    public String heart(@PathVariable Long id, Authentication auth) {
        User user = userService.getLoginUserByLoginId(auth.getName());
        heartService.heart(id, user.getId());

        return "redirect:/board/" + id;
    }

    @GetMapping("/category/{id}")
    public String findByCategoryId(@PathVariable Long id, Model model, @PageableDefault(page = 1) Pageable pageable) {
        Page<BoardDTO> boards = boardService.findByCategoryId(id, pageable);

        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boards.getTotalPages()) ? startPage + blockLimit - 1 : boards.getTotalPages();

        model.addAttribute("boards", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentCategoryId", id);

        return "index";
    }

    /*
    로그인 아이디로 글 목록 return
     */
    @GetMapping("/user/{loginId}")
    public String findByLoginId(@PathVariable String loginId, Model model, @PageableDefault(page = 1) Pageable pageable) {
        Page<BoardDTO> boards = boardService.findByUserLoginId(loginId, pageable);

        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boards.getTotalPages()) ? startPage + blockLimit - 1 : boards.getTotalPages();

        model.addAttribute("boards", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageName", loginId + "님이 작성한 글");

        return "index";
    }

    @GetMapping("/heart/user/{loginId}")
    public String findByHeart(@PathVariable String loginId, Model model) {
        User user = userService.getLoginUserByLoginId(loginId);
        List<BoardDTO> boards = heartService.findByHeart(user);

        model.addAttribute("boards", boards);
        model.addAttribute("pageName", loginId + "님이 좋아요한 글");

        return "index";
    }

}
