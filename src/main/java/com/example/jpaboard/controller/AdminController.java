package com.example.jpaboard.controller;

import com.example.jpaboard.dto.CategoryDTO;
import com.example.jpaboard.dto.UserDTO;
import com.example.jpaboard.service.CategoryService;
import com.example.jpaboard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;
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

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication auth) {
        if (auth != null) {
            return userService.checkAdmin(auth.getName());
        }
        return false;
    }

    @GetMapping("/admin")
    public String adminForm() {
        return "admin/admin";
    }

    @GetMapping("/category/new")
    public String createForm() {
        return "category/saveCategory";
    }

    @PostMapping("/category/new")
    public String create(@ModelAttribute CategoryDTO categoryDTO) {
        categoryService.save(categoryDTO);
        return "redirect:/admin/category/list";
    }

    @GetMapping("/category/list")
    public String list() {
        return "category/listCategory";
    }

    @GetMapping("/category/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        CategoryDTO category = categoryService.findById(id);
        model.addAttribute("category", category);

        return "category/updateCategory";
    }

    @PostMapping("/category/update")
    public String update(@ModelAttribute CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);

        return "redirect:/admin/category/list";
    }

    @GetMapping("/category/delete/{id}")
    public String delete(@PathVariable Long id) {
        categoryService.delete(id);

        return "redirect:/admin/category/list";
    }

    @GetMapping("/user/list")
    public String userList(Model model) {
        List<UserDTO> users = userService.list();
        model.addAttribute("users", users);

        return "admin/listUser";
    }

    @GetMapping("/user/update/{id}")
    public String updateRole(@PathVariable Long id) {
        userService.updateRole(id);

        return "redirect:/admin/user/list";
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<?> IllegalArgumentHandler(IllegalStateException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
