package com.tfg.configuration;

import com.tfg.physiotherapist.Physiotherapist;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class PhysiotherapistDetails implements UserDetails {
    private final Physiotherapist physiotherapist;
    private final Collection<? extends GrantedAuthority> authorities;

    public PhysiotherapistDetails(Physiotherapist physiotherapist) {
        this.physiotherapist = physiotherapist;
        this.authorities = physiotherapist.getRoles().stream()
                .map(role -> (GrantedAuthority) role::name)
                .toList();
    }

    public String getName() {
        return physiotherapist.getName();
    }

    public String getSurname() {
        return physiotherapist.getSurname();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return physiotherapist.getPassword();
    }

    @Override
    public String getUsername() {
        return String.valueOf(physiotherapist.getId().value());
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
