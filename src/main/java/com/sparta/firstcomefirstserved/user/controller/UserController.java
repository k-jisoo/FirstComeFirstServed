package com.sparta.firstcomefirstserved.user.controller;

import com.sparta.firstcomefirstserved.user.dto.LoginRequestDto;
import com.sparta.firstcomefirstserved.user.dto.SignupRequestDto;
import com.sparta.firstcomefirstserved.user.entity.User;
import com.sparta.firstcomefirstserved.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignupRequestDto signupRequestDto) {
        userService.registerUser(signupRequestDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        return userService.verifyUser(token);
    }

}