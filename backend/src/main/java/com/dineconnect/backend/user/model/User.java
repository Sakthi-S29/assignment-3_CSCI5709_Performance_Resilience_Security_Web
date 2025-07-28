package com.dineconnect.backend.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

@Document(collection = "_users")
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class User implements CustomUserDetails {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private Role role;

    public User(String email, String username, String password, Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.role.getAuthority());
    }

    @Override
    public boolean isAccountNonExpired() {
        return CustomUserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return CustomUserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return CustomUserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return CustomUserDetails.super.isEnabled();
    }
}
