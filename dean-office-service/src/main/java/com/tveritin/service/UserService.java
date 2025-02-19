package com.tveritin.service;

import com.tveritin.bankservice.dto.CreateUserRequest;
import com.tveritin.bankservice.dto.CreateUserResponse;
import com.tveritin.bankservice.dto.OpenAccountRequest;
import com.tveritin.entity.User;
import com.tveritin.mapper.UserMapper;
import com.tveritin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public CreateUserResponse createUser(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        User createdUser = userRepository.save(user);

        return userMapper.toResponse(createdUser);
    }

    public void openAccount(OpenAccountRequest request) {
        // Implementation for opening an account
    }
}
