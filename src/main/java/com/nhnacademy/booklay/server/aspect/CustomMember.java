package com.nhnacademy.booklay.server.aspect;

import com.nhnacademy.booklay.server.constant.Roles;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class CustomMember implements UserDetails {

    private String username;

    private String password;

    private List<Roles> authorities;

    private String accessToken;

    private String uuid;

    private String refreshToken;

    public CustomMember(String username, String password, List<Roles> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                          .map(m -> new SimpleGrantedAuthority(m.getValue()))
                          .collect(Collectors.toList());
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
