package com.example.jpaboard.service;

import com.example.jpaboard.dto.BoardDTO;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;

    public Long save(BoardDTO boardDTO) {
        Board board = Board.toBoard(boardDTO);
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

    public BoardDTO findById(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);

        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            return BoardDTO.toBoardDTO(board);
        } else {
            return null;
        }
    }

    public BoardDTO update(BoardDTO boardDTO) {
        Board board = Board.toBoard(boardDTO);
        boardRepository.save(board);
        return findById(board.getId());
    }

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
}
