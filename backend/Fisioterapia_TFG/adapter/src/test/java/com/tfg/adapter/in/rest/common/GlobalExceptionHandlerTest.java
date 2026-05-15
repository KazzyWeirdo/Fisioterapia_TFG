package com.tfg.adapter.in.rest.common;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.application.exceptions.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GlobalExceptionHandlerTest {

    @RestController
    static class StubController {
        @GetMapping("/test/invalid-id")
        public void throwInvalidId() { throw new InvalidIdException(); }

        @GetMapping("/test/invalid-token")
        public void throwInvalidToken() { throw new InvalidTokenException("invalid token"); }

        @GetMapping("/test/bad-credentials")
        public void throwBadCredentials() { throw new BadCredentialsException("bad"); }

        @GetMapping("/test/illegal-argument")
        public void throwIllegalArgument() { throw new IllegalArgumentException("bad arg"); }

        @GetMapping("/test/generic-exception")
        public void throwGenericException() throws Exception { throw new Exception("oops"); }
    }

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StubController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void handleInvalidIdException_returns404() throws Exception {
        mockMvc.perform(get("/test/invalid-id"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void handleInvalidTokenException_returns400() throws Exception {
        mockMvc.perform(get("/test/invalid-token"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void handleBadCredentialsException_returns401() throws Exception {
        mockMvc.perform(get("/test/bad-credentials"))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void handleIllegalArgumentException_returns400() throws Exception {
        mockMvc.perform(get("/test/illegal-argument"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void handleGlobalException_returns500() throws Exception {
        mockMvc.perform(get("/test/generic-exception"))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
