package com.example.jpaboard.controller;

import com.example.jpaboard.dto.CategoryDTO;
import com.example.jpaboard.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @ModelAttribute("categories")
    public List<CategoryDTO> categories() {
        List<CategoryDTO> categories = categoryService.list();

        return categories;
    }

    @GetMapping("/new")
    public String createForm() {
        return "category/saveCategory";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute CategoryDTO categoryDTO) {
        categoryService.save(categoryDTO);
        return "redirect:/category/list";
    }

    @GetMapping("/list")
    public String list() {
        return "category/listCategory";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        CategoryDTO category = categoryService.findById(id);
        model.addAttribute("category", category);

        return "category/updateCategory";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);

        return "redirect:/category/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        categoryService.delete(id);

        return "redirect:/category/list";
    }


    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<?> IllegalArgumentHandler(IllegalStateException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }





}
