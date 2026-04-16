package com.tfg.adapter.out.springsecurity;

import com.tfg.pojos.springsecurity.AuthenticatedUser;
import com.tfg.port.out.springsecurity.CredentialsValidatorPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringSecurityCredentialsAdapter implements CredentialsValidatorPort {

    private final AuthenticationConfiguration authenticationConfiguration;

    public SpringSecurityCredentialsAdapter(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Override
    public AuthenticatedUser validate(String subject, String password) {
        try {
            AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(subject, password)
            );

            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return new AuthenticatedUser(authentication.getName(), roles);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
