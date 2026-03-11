package com.practice.taskmanager.mapper;

import com.practice.taskmanager.dto.UserDto;
import com.practice.taskmanager.enm.Role;
import com.practice.taskmanager.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {


    private  final PasswordEncoder passwordEncoder;

    public  User mapUser(UserDto userDto){
        User user = new User();
        user.setUsername(userDto.getUserName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // plain text (for now)
        user.setEmail(userDto.getEmail());
        user.setMobilenumber(userDto.getMobileNumber());
        user.setRole(Role.USER.name());

        return user;

    }
}
