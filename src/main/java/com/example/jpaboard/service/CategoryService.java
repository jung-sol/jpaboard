package com.example.jpaboard.service;

import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.dto.CategoryDTO;
import com.example.jpaboard.entity.Board;
import com.example.jpaboard.entity.Category;
import com.example.jpaboard.repository.BoardRepository;
import com.example.jpaboard.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Long save(CategoryDTO categoryDTO) {
        Category category = Category.toCategory(categoryDTO);
        categoryRepository.save(category);
        return category.getId();
    }

    public List<CategoryDTO> list() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryDTOList.add(CategoryDTO.toCategoryDTO(category));
        }
        return categoryDTOList;
    }

    public CategoryDTO findById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            CategoryDTO categoryDTO = CategoryDTO.toCategoryDTO(optionalCategory.get());
            return categoryDTO;
        } else {
            return null;
        }
    }

    @Transactional
    public Long update(CategoryDTO categoryDTO) {
        Category category = Category.toCategory(categoryDTO);
        categoryRepository.save(category);
        return category.getId();
    }

    @Transactional
    public Long delete(Long id) {
        vaildateExistBoard(id);
        categoryRepository.deleteById(id);
        return id;
    }

    private void vaildateExistBoard(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        List<Board> boards = boardRepository.findByCategory(optionalCategory.get());
        if (!boards.isEmpty()) {
            throw new IllegalStateException("게시글이 존재하여 게시판을 삭제할 수 없습니다.");
        }

    }
}
