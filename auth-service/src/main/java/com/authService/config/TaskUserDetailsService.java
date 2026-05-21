package com.authService.config;

import com.authService.entity.User;
import com.common.exception.NotFoundException;
import com.authService.repository.UserRepository;
import com.common.security.UserPrincipal;
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
    public com.common.security.UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new NotFoundException("User details not found for the id : "+ username));
        return new UserPrincipal(user.getUsername(),user.getPassword(),user.getBranchcode(),"",0, List.of());
    }
}
