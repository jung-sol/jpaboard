package com.example.jpaboard.repository;

import com.example.jpaboard.entity.Board;
import com.example.jpaboard.entity.Comment;
import com.example.jpaboard.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.jpaboard.entity.QComment.comment;

import java.util.List;

@Repository
public class CommentCustomRepository {

    private JPAQueryFactory jpaQueryFactory;

    public CommentCustomRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Comment> findAllByBoard(Long board_id) {
        return jpaQueryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.board.id.eq(board_id))
                .orderBy(comment.parent.id.asc().nullsFirst(), comment.createdTime.asc())
                .fetch();
    }
}
