package com.taskmanager.config;

import java.util.List;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
@Getter
public class UserPrincipal {
    private String username;
    private String branchCode;
    private String jti;
    private long expirationTime;
    private List<GrantedAuthority> authorities;

    public UserPrincipal(String username, String branchCode,String jti,long expirationTime, List<GrantedAuthority> authorities) {
        this.username = username;
        this.branchCode = branchCode;
        this.jti = jti;
        this.expirationTime = expirationTime;
        this.authorities = authorities;
    }

    public String getUsername() { return username; }
    public String getBranchCode() { return branchCode; }
    public List<GrantedAuthority> getAuthorities() { return authorities; }
}