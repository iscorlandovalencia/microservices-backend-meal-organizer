package com.valencia.user.controller;

import com.valencia.user.dto.AuthResponse;
import com.valencia.user.dto.LoginRequest;
import com.valencia.user.dto.RegisterRequest;
import com.valencia.user.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_shouldReturnToken() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("orlando");
        request.setPassword("secure123");

        AuthResponse expectedResponse = new AuthResponse("mocked-jwt-token");
        when(authService.login(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mocked-jwt-token", response.getBody().getToken());
        verify(authService, times(1)).login(request);
    }

    @Test
    void register_shouldReturnToken() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("orlando");
        request.setPassword("secure123");

        AuthResponse expectedResponse = new AuthResponse("new-user-jwt-token");
        when(authService.register(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.register(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("new-user-jwt-token", response.getBody().getToken());
        verify(authService, times(1)).register(request);
    }
    @Test
    void login_shouldReturnUnauthorized_whenUserNotFound() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("no-existe");
        request.setPassword("1234");

        when(authService.login(request)).thenThrow(new RuntimeException("Usuario no encontrado"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.login(request);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(authService, times(1)).login(request);
    }

    @Test
    void login_shouldReturnUnauthorized_whenPasswordIncorrect() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("orlando");
        request.setPassword("incorrecta");

        when(authService.login(request)).thenThrow(new RuntimeException("Credenciales inválidas"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.login(request);
        });

        assertEquals("Credenciales inválidas", exception.getMessage());
        verify(authService, times(1)).login(request);
    }

    @Test
    void register_shouldReturnConflict_whenUserAlreadyExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("orlando");
        request.setPassword("secure123");

        when(authService.register(request)).thenThrow(new RuntimeException("Usuario ya existe"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.register(request);
        });

        assertEquals("Usuario ya existe", exception.getMessage());
        verify(authService, times(1)).register(request);
    }
    
}