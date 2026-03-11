package com.practice.taskmanager.controller;

import com.practice.taskmanager.dto.ResponseDto;
import com.practice.taskmanager.dto.UserDto;
import com.practice.taskmanager.service.RegisterLoginService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RegisterLoginController {

    private final RegisterLoginService registerLoginService;

    public RegisterLoginController(RegisterLoginService registerLoginService) {
        this.registerLoginService = registerLoginService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Valid @RequestBody UserDto userDto) {
        registerLoginService.register(userDto);
        System.out.println("1");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("User registered successfully."));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody UserDto userDto) {
        registerLoginService.login(userDto.getUserName(), userDto.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("User logged in successful."));
    }
}
