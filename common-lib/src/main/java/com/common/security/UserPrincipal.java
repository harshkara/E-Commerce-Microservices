package com.common.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class UserPrincipal implements UserDetails {

    private String username;
    private String password;
    private String branchcode;
    private String jti;
    private long expirationTime;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String username,
                         String password,
                         String branchcode,
                         String jti,
                         long expirationTime,
                         Collection<? extends GrantedAuthority> authorities) {

        this.username = username;
        this.password = password;
        this.branchcode = branchcode;
        this.jti = jti;
        this.expirationTime = expirationTime;
        this.authorities = authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
