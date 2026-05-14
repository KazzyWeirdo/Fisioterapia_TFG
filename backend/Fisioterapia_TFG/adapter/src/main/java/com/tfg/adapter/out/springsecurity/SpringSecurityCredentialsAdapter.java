package com.tfg.adapter.out.springsecurity;

import com.tfg.configuration.PhysiotherapistDetails;
import com.tfg.pojos.springsecurity.AuthenticatedUser;
import com.tfg.port.out.springsecurity.CredentialsValidatorPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringSecurityCredentialsAdapter implements CredentialsValidatorPort {

    private final AuthenticationManager authenticationManager;

    public SpringSecurityCredentialsAdapter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticatedUser validate(String subject, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(subject, password)
            );

            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            PhysiotherapistDetails details = (PhysiotherapistDetails) authentication.getPrincipal();
            return new AuthenticatedUser(authentication.getName(), roles, details.getName(), details.getSurname());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
