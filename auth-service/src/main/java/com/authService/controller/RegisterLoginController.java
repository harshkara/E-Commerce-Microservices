package com.authService.controller;

import com.common.dto.ResponseDto;
import com.authService.dto.UserDto;
import com.authService.service.RegisterLoginService;
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
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("User registered successfully."));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody UserDto userDto) {
        String resp = registerLoginService.login(userDto.getUserName(), userDto.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(resp));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(){
        registerLoginService.logout();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("User logout successfully."));
    }
}
