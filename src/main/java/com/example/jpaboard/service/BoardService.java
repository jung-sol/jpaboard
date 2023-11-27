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

    public List<BoardDTO> list() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (Board board : boardList) {
            boardDTOList.add(BoardDTO.toBoardDTO(board));
        }
        return boardDTOList;
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;

        // 페이지당 3개 글, id 기준으로 내림차순 정렬
        Page<Board> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        return boardEntities.map(BoardDTO::toBoardDTO);
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

    public List<BoardDTO> findByCategoryId(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        List<Board> boards = boardRepository.findByCategory(optionalCategory.get());
        List<BoardDTO> boardDTOS = new ArrayList<>();
        for (Board board : boards) {
            boardDTOS.add(BoardDTO.toBoardDTO(board));
        }

        return boardDTOS;
    }

    public List<BoardDTO> findByUserLoginId(String userLoginId) {
        Optional<User> optionalUser = userRepository.findByLoginId(userLoginId);

        if (optionalUser.isEmpty()) {
            return null;
        }

        List<Board> boards = boardRepository.findByUserId(optionalUser.get());
        List<BoardDTO> boardDTOS = new ArrayList<>();
        for (Board board : boards) {
            boardDTOS.add(BoardDTO.toBoardDTO(board));
        }

        return boardDTOS;
    }


}
