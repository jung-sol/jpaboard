package com.example.jpaboard.entity;

import com.example.jpaboard.dto.CategoryDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(length = 20)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Board> boards = new ArrayList<>();

    public static Category toCategory(CategoryDTO categoryDTO) {
        Category category = new Category();

        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());

        return category;
    }
}
