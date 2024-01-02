package com.example.jpaboard.repository;

import com.example.jpaboard.entity.Board;
import com.example.jpaboard.entity.Comment;
import com.example.jpaboard.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.example.jpaboard.entity.QComment.comment;
import static com.example.jpaboard.entity.QHeart.heart;
import static com.example.jpaboard.entity.QBoard.board;

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

    public Page<Board> findBoardByHeart(Long user_id, Pageable pageable) {
        List<Board> boards = jpaQueryFactory
                .select(board)
                .from(board)
                .join(heart)
                .on(board.eq(heart.board))
                .where(heart.user.id.eq(user_id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(board.count())
                .from(board)
                .join(heart)
                .on(board.eq(heart.board))
                .where(heart.user.id.eq(user_id))
                .fetchOne();

        return new PageImpl<>(boards, pageable, count);
    }

}
