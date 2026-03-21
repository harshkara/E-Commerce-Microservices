package com.practice.authService.config;

import com.practice.authService.entity.User;
import com.practice.authService.exception.NotFoundException;
import com.practice.authService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new NotFoundException("User details not found for the id : "+ username));
        return new UserPrincipal(user.getUsername(),user.getPassword(),user.getBranchcode(), List.of());
    }
}
