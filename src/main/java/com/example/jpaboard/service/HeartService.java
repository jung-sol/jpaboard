package com.example.jpaboard.service;

import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.entity.Board;
import com.example.jpaboard.entity.Heart;
import com.example.jpaboard.entity.User;
import com.example.jpaboard.repository.BoardRepository;
import com.example.jpaboard.repository.HeartRepository;
import com.example.jpaboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HeartService {

    private final HeartRepository heartRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void heart(Long board_id, Long user_id) {
        Long heart_id = checkStatusLike(board_id, user_id);

        if (heart_id != null) {
            heartRepository.deleteById(heart_id);

        } else {
            Optional<Board> optionalBoard = boardRepository.findById(board_id);
            if (optionalBoard.isEmpty()) {
                throw new IllegalStateException("잘못된 접근입니다.");
            }

            Optional<User> optionalUser = userRepository.findById(user_id);
            if (optionalUser.isEmpty()) {
                throw new IllegalStateException("존재하지 않은 회원입니다.");
            }

            Heart heart = new Heart();
            heart.setBoard(optionalBoard.get());
            heart.setUser(optionalUser.get());


            heartRepository.save(heart);
        }
    }

    public Long checkStatusLike(Long board_id, Long user_id) {
        Optional<Heart> optionalHeart = heartRepository.findByBoardIdAndUserId(board_id, user_id);

        if (optionalHeart.isEmpty()) {
            return null;
        }
        return optionalHeart.get().getId();
    }

    public List<BoardDTO> findByHeart(User user) {
        List<Heart> heartList = user.getHeartList();
        List<BoardDTO> boards = new ArrayList<>();
        for (Heart heart : heartList) {
            Optional<Board> optionalBoard = boardRepository.findById(heart.getBoard().getId());
            boards.add(BoardDTO.toBoardDTO(optionalBoard.get()));
        }
        return boards;
    }
}
