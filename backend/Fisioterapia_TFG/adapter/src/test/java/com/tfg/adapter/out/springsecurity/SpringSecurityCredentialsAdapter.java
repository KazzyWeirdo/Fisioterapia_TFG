package com.tfg.adapter.out.springsecurity;

import com.tfg.exceptions.BadCredentialsException;
import com.tfg.pojos.springsecurity.AuthenticatedUser;
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
        mockAuthentication = new UsernamePasswordAuthenticationToken(
                "123",
                null,
                List.of(new SimpleGrantedAuthority("USER"))
        );
    }

    @Test
    void validate_shouldReturnAuthenticatedUser_whenCredentialsAreCorrect() {
        when(authenticationManager.authenticate(any())).thenReturn(mockAuthentication);

        AuthenticatedUser result = adapter.validate("123", "password");

        assertThat(result.subject()).isEqualTo("123");
        assertThat(result.roles()).containsExactly("USER");
    }

    @Test
    void validate_shouldMapMultipleRoles_whenUserHasSeveralAuthorities() {
        Authentication multiRoleAuth = new UsernamePasswordAuthenticationToken(
                "123",
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
        // Given
        when(authenticationManager.authenticate(any())).thenReturn(mockAuthentication);

        // When
        adapter.validate("123", "password");

        // Then
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("123", "password")
        );
    }

    @Test
    void validate_shouldThrow_whenCredentialsAreWrong() {
        // Given
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException());

        // When / Then
        assertThatThrownBy(() -> adapter.validate("123", "wrongpassword"))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid credentials provided.");
    }
}