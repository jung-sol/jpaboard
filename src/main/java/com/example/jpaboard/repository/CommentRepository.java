package com.example.jpaboard.repository;

import com.example.jpaboard.entity.Board;
import com.example.jpaboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    public List<Comment> findByBoard(Board board);
}
