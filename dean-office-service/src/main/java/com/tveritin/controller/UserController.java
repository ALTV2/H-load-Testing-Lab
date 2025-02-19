package com.tveritin.controller;

import com.tveritin.bankservice.api.UsersApi;
import com.tveritin.bankservice.dto.CreateUserRequest;
import com.tveritin.bankservice.dto.CreateUserResponse;
import com.tveritin.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController implements UsersApi {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @PostMapping
    public ResponseEntity<CreateUserResponse> usersPost(@Valid @RequestBody CreateUserRequest createUserRequest) {
        CreateUserResponse userResponse = userService.createUser(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

}
