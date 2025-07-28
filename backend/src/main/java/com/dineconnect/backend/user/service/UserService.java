package com.dineconnect.backend.user.service;

import com.dineconnect.backend.user.exception.UserAlreadyExistsException;
import com.dineconnect.backend.user.model.Role;
import com.dineconnect.backend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.dineconnect.backend.user.respository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //Create a new user
    public User createUser(String email,String username, String password, Boolean isAdmin) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        return userRepository.save(
                User.builder()
                        .email(email)
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .role(isAdmin? Role.ADMIN : Role.USER)
                        .build()
        );
    }

    //Reset password for an existing user
    public User resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
}
