package com.practice.taskmanager.config;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;

public class UserPrincipal {
    private String username;
    private String branchCode;
    private List<GrantedAuthority> authorities;

    public UserPrincipal(String username, String branchCode, List<GrantedAuthority> authorities) {
        this.username = username;
        this.branchCode = branchCode;
        this.authorities = authorities;
    }

    public String getUsername() { return username; }
    public String getBranchCode() { return branchCode; }
    public List<GrantedAuthority> getAuthorities() { return authorities; }
}