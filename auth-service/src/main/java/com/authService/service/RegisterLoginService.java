package com.authService.service;

import com.common.security.UserPrincipal;
import com.authService.dto.UserDto;
import com.authService.entity.User;
import com.common.exception.AlreadyPresentException;
import com.authService.mapper.UserMapper;
import com.authService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterLoginService {

    private final RedisTemplate<String,String> redisTemplate;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authManager;
    private final com.common.security.JwtService jwtService;

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

    public void logout() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try{
            redisTemplate.opsForValue().set(
                    "blacklist:" + userPrincipal.getJti(),
                    "true",
                    Duration.ofMillis(userPrincipal.getExpirationTime() - System.currentTimeMillis())
            );
        }catch (Exception ex) {

            log.error(
                    "Redis unavailable. Skipping blacklist check.",
                    ex
            );

        }



    }
}
