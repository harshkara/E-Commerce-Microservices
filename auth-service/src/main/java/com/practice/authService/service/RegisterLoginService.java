package com.practice.authService.service;

import com.practice.authService.dto.UserDto;
import com.practice.authService.entity.User;
import com.practice.authService.enm.Role;
import com.practice.authService.exception.AlreadyPresentException;
import com.practice.authService.exception.InvalidCredentialsException;
import com.practice.authService.exception.NotFoundException;
import com.practice.authService.mapper.UserMapper;
import com.practice.authService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public void register(UserDto userDto) {

        if (userRepository.findByUsername(userDto.getUserName()).isPresent()) {
             throw new AlreadyPresentException("User already present with this username . Please try with different name");
        }

        User user =  userMapper.mapUser(userDto);
        userRepository.save(user);
        return;
    }

    public void login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found please register first."));

        if (!passwordEncoder.matches(password,user.getPassword())) {
            throw new InvalidCredentialsException("Wrong password entered.");
        }

        return;
    }
}
