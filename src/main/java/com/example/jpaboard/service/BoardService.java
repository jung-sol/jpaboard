package com.example.jpaboard.service;

import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.dto.UserDTO;
import com.example.jpaboard.entity.Board;
import com.example.jpaboard.entity.Category;
import com.example.jpaboard.entity.User;
import com.example.jpaboard.repository.BoardRepository;
import com.example.jpaboard.repository.CategoryRepository;
import com.example.jpaboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(BoardDTO boardDTO, String loginId) {
        Board board = Board.toBoard(boardDTO);
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);

        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("없는 사용자입니다.");
        }

        board.setUser(optionalUser.get());    // user 저장
        boardRepository.save(board);

        return board.getId();
    }

    public BoardDTO findById(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);

        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            return BoardDTO.toBoardDTO(board);
        } else {
            return null;
        }
    }

    @Transactional
    public Long update(Long boardId, Category category, String title, String content) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);

        if (optionalBoard.isEmpty()) {
            return null;
        }

        Board board = optionalBoard.get();
        board.updateBoard(category, title, content);

        return board.getId();
    }

    @Transactional
    public Long delete(Long id) {
        boardRepository.deleteById(id);
        return id;
    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public Page<BoardDTO> list(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;

        // 페이지당 3개 글, id 기준으로 내림차순 정렬
        Page<Board> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        return boardEntities.map(BoardDTO::toBoardDTO);
    }

    public Page<BoardDTO> findByCategoryId(Long categoryId, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Page<Board> boards = boardRepository.findByCategory(optionalCategory.get(), PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        return boards.map(BoardDTO::toBoardDTO);
    }

    public Page<BoardDTO> findByUserLoginId(String userLoginId, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;

        Optional<User> optionalUser = userRepository.findByLoginId(userLoginId);

        if (optionalUser.isEmpty()) {
            return null;
        }

        Page<Board> boards = boardRepository.findByUserId(optionalUser.get(), PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        return boards.map(BoardDTO::toBoardDTO);
    }

    public Page<BoardDTO> searchByTitle(String keyword, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;

        Page<Board> boards = boardRepository.findByTitleContaining(keyword, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        return boards.map(BoardDTO::toBoardDTO);
    }


}
