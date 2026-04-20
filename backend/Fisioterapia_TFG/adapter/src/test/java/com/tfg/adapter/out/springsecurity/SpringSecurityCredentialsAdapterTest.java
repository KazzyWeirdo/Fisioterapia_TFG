package com.tfg.adapter.out.springsecurity;

import com.tfg.configuration.PhysiotherapistDetails;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.pojos.springsecurity.AuthenticatedUser;
import org.springframework.security.authentication.BadCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringSecurityCredentialsAdapterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private SpringSecurityCredentialsAdapter adapter;

    private Authentication mockAuthentication;

    @BeforeEach
    void setUp() {
        PhysiotherapistDetails principal = new PhysiotherapistDetails(
                PhysiotherapistFactory.createTestPsychiatrist("physio@example.com", "password")
        );
        mockAuthentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority("USER"))
        );
    }

    @Test
    void validate_shouldReturnAuthenticatedUser_whenCredentialsAreCorrect() {
        when(authenticationManager.authenticate(any())).thenReturn(mockAuthentication);

        AuthenticatedUser result = adapter.validate("123", "password");

        assertThat(result.subject()).isEqualTo(mockAuthentication.getName());
        assertThat(result.roles()).containsExactly("USER");
        assertThat(result.name()).isEqualTo(PhysiotherapistFactory.NAME);
        assertThat(result.surname()).isEqualTo(PhysiotherapistFactory.SURNAME);
    }

    @Test
    void validate_shouldMapMultipleRoles_whenUserHasSeveralAuthorities() {
        PhysiotherapistDetails principal = new PhysiotherapistDetails(
                PhysiotherapistFactory.createTestPsychiatrist("physio@example.com", "password")
        );
        Authentication multiRoleAuth = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(
                        new SimpleGrantedAuthority("USER"),
                        new SimpleGrantedAuthority("ADMIN")
                )
        );
        when(authenticationManager.authenticate(any())).thenReturn(multiRoleAuth);

        AuthenticatedUser result = adapter.validate("123", "password");

        assertThat(result.roles()).containsExactlyInAnyOrder("USER", "ADMIN");
    }

    @Test
    void validate_shouldCallAuthenticationManager_withCorrectToken() {
        when(authenticationManager.authenticate(any())).thenReturn(mockAuthentication);

        adapter.validate("123", "password");

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("123", "password")
        );
    }

    @Test
    void validate_shouldThrow_whenCredentialsAreWrong() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials provided."));

        assertThatThrownBy(() -> adapter.validate("123", "wrongpassword"))
                .isInstanceOf(RuntimeException.class);
    }
}