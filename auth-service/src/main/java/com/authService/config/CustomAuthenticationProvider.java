package com.authService.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final TaskUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        com.common.security.UserPrincipal userPrincipal = userDetailsService.loadUserByUsername(userName);

        //before checking password we can check whether user is active or not multiple checks can be there


        if(passwordEncoder.matches(password,userPrincipal.getPassword())){
            com.common.security.UserPrincipal safePrincipal = new com.common.security.UserPrincipal(
                    userPrincipal.getUsername(),
                    null, // remove password
                    userPrincipal.getBranchcode(),userPrincipal.getJti(),userPrincipal.getExpirationTime(),
                    userPrincipal.getAuthorities()
            );
            return new UsernamePasswordAuthenticationToken(safePrincipal,null,safePrincipal.getAuthorities());
        }else{
            throw new BadCredentialsException("Invalid Password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
