package com.task.process_approval_system.services;

import com.task.process_approval_system.models.User;
import com.task.process_approval_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public UserService(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    public User registerUser(User user) {
        String hashedPassword = passwordService.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User findUser(String loginID) {
        return userRepository.findById(Long.parseLong(loginID)).orElseThrow(() -> new RuntimeException("User not found"));
    }

}
