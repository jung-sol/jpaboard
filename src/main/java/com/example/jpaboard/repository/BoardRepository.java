package com.example.jpaboard.repository;

import com.example.jpaboard.entity.Board;
import com.example.jpaboard.entity.Category;
import com.example.jpaboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query(value = "update Board b set b.boardHits = b.boardHits + 1 where b.id = :id")
    void updateHits(@Param("id") Long id);

    @Query(value = "select b from Board as b where b.category = :category")
    List<Board> findByCategory(@Param("category") Category category);

    @Query(value = "select b from Board as b where b.user = :user")
    List<Board> findByUserId(@Param("user") User user);

}
