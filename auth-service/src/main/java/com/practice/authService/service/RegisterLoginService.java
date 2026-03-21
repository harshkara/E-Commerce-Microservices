package com.practice.authService.service;

import com.practice.authService.config.UserPrincipal;
import com.practice.authService.dto.UserDto;
import com.practice.authService.entity.User;
import com.practice.authService.enm.Role;
import com.practice.authService.exception.AlreadyPresentException;
import com.practice.authService.exception.InvalidCredentialsException;
import com.practice.authService.exception.NotFoundException;
import com.practice.authService.mapper.UserMapper;
import com.practice.authService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterLoginService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public void register(UserDto userDto) {

        if (userRepository.findByUsername(userDto.getUserName()).isPresent()) {
             throw new AlreadyPresentException("User already present with this username . Please try with different name");
        }

        User user =  userMapper.mapUser(userDto);
        userRepository.save(user);
        return;
    }

    public String login(String username, String password) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return jwtService.generateToken(userPrincipal.getUsername(),userPrincipal.getBranchcode());
    }
}
