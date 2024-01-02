package com.example.jpaboard.repository;

import com.example.jpaboard.entity.Board;
import com.example.jpaboard.entity.Heart;
import com.example.jpaboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByBoardIdAndUserId(Long boardId, Long userId);

}
