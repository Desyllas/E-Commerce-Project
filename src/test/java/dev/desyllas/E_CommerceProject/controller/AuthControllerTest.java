package dev.desyllas.E_CommerceProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.desyllas.E_CommerceProject.dto.AuthRequest;
import dev.desyllas.E_CommerceProject.dto.AuthResponse;
import dev.desyllas.E_CommerceProject.security.AuthenticationService;
import dev.desyllas.E_CommerceProject.security.JwtService;
import dev.desyllas.E_CommerceProject.security.TokenBlacklistService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private TokenBlacklistService tokenBlacklistService;

    @Test
    void login_success() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        AuthResponse response = new AuthResponse("dummy-token");
        when(authService.authenticate(any(AuthRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-token"));

        verify(authService, times(1)).authenticate(any(AuthRequest.class));
    }

    @Test
    void register_success() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setEmail("newuser@example.com");
        request.setPassword("password");

        AuthResponse response = new AuthResponse("new-token");
        when(authService.register(any())).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("new-token"));

        verify(authService, times(1)).register(any());
    }
}
