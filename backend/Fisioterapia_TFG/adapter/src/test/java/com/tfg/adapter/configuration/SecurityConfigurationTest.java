package com.tfg.adapter.configuration;


import com.tfg.adapter.out.persistence.JpaTestContainerConfig;
import com.tfg.service.physiotherapist.LogPhysiotherapistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
class SecurityConfigurationTest extends JpaTestContainerConfig {

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", getInstance()::getJdbcUrl);
        registry.add("spring.datasource.username", getInstance()::getUsername);
        registry.add("spring.datasource.password", getInstance()::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LogPhysiotherapistService logPhysiotherapistService;

    @Test
    void givenWhiteListUrl_whenAccessWithoutAuth_thenOk() throws Exception {
        mockMvc.perform(post("/physiotherapist/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_USER")
    void givenUserScope_whenAccessUserEndpoints_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/indiba/session/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_ADMIN")
    void givenAdminScope_whenAccessAdminEndpoints_thenOk() throws Exception {
        mockMvc.perform(post("/physiotherapist/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNoAuth_whenAccessProtectedEndpoint_thenUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/indiba/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_USER")
    void givenUserScope_whenAccessAdminEndpoints_thenForbidden() throws Exception {
        mockMvc.perform(post("/physiotherapist/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\"}"))
                .andExpect(status().isForbidden());
    }
}
