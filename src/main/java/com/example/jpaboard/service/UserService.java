package com.example.jpaboard.service;

import com.example.jpaboard.dto.JoinRequest;
import com.example.jpaboard.dto.LoginRequest;
import com.example.jpaboard.dto.UpdatePasswordRequest;
import com.example.jpaboard.entity.User;
import com.example.jpaboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void join(JoinRequest req) {
        userRepository.save(req.toUser());
    }

    @Transactional
    public void join2(JoinRequest req) {
        userRepository.save(req.toUser(encoder.encode(req.getPassword())));
    }

    @Transactional
    public void updatePassword(String loginId, String password) {
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        User user = optionalUser.get();
        user.updatePassword(encoder.encode(password));
    }

    @Transactional
    public void updateNickname(String loginId, String nickname) {
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        User user = optionalUser.get();
        user.setNickname(nickname);
    }

    public User login(LoginRequest req) {
        Optional<User> optionalUser = userRepository.findByLoginId(req.getLoginId());
        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(req.getPassword())) {
            return null;
        }

        return user;
    }

    public User getLoginUserById(Long id) {
        if (id == null) {
            return null;
        }

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return null;
        }

        return  optionalUser.get();
    }

    public User getLoginUserByLoginId(String loginId) {
        if (loginId == null) {
            return null;
        }

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);

        if (optionalUser.isEmpty()) {
            return null;
        }

        return optionalUser.get();
    }

    public boolean checkPassword(String inputPassword, String inDBPassword) {
        return encoder.matches(inputPassword, inDBPassword);
    }
}
