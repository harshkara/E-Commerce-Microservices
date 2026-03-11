package com.practice.authService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    @NotBlank(message="Username is required field.")
    @Size(min=3,max=10,message = "Username must be within 3 to 10 characters")
    private String userName;

    @NotBlank(message="Password is required field.")
    @Size(min=8,message = "Password must be atleast 8 characters long.")
    private String password;

    @NotBlank(message="Email is required field.")
    @Email(message = "Invalid email format")
    private String email;


    @NotBlank(message="Mobile No. cannot be blank.")
    @Pattern(regexp="^\\d{10}$",message="Mobile number must be numeric and contain 10 digits.")
    private String mobileNumber;
}
