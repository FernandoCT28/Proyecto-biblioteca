package com.api.biblioteca.auth;

import com.api.biblioteca.controller.auth.AuthController;
import com.api.biblioteca.dto.auth.AuthDto;
import com.api.biblioteca.dto.auth.LoginDto;
import com.api.biblioteca.dto.auth.RegisterDto;
import com.api.biblioteca.exception.ErrorResponse;
import com.api.biblioteca.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        LoginDto loginDto = new LoginDto("john@example.com", "password");
        AuthDto authDto = new AuthDto("mockToken");

        when(authService.login(any(LoginDto.class))).thenReturn(authDto);

        // Act
        ResponseEntity<AuthDto> response = authController.Login(loginDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authDto, response.getBody());
    }

    @Test
    void testLoginUnauthorized() {
        // Arrange
        LoginDto loginDto = new LoginDto("john@example.com", "password");

        when(authService.login(any(LoginDto.class))).thenReturn(null);

        // Act
        ResponseEntity<AuthDto> response = authController.Login(loginDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testLoginInternalServerError() {
        // Arrange
        LoginDto loginDto = new LoginDto("john@example.com", "password");

        when(authService.login(any(LoginDto.class))).thenThrow(new RuntimeException("Internal error"));

        // Act
        ResponseEntity<AuthDto> response = authController.Login(loginDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testRegisterSuccess() {
        // Arrange
        RegisterDto registerDto = new RegisterDto("John", "john@example.com", "password");
        AuthDto authDto = new AuthDto("mockToken");

        when(authService.register(any(RegisterDto.class))).thenReturn(authDto);

        // Act
        ResponseEntity<?> response = authController.register(registerDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authDto, response.getBody());
    }

    @Test
    void testRegisterBadRequest() {
        // Arrange
        RegisterDto registerDto = new RegisterDto("John", "john@example.com", "password");

        when(authService.register(any(RegisterDto.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        // Act
        ResponseEntity<?> response = authController.register(registerDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid data", ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    void testRegisterInternalServerError() {
        // Arrange
        RegisterDto registerDto = new RegisterDto("John", "john@example.com", "password");

        when(authService.register(any(RegisterDto.class))).thenThrow(new RuntimeException("Internal error"));

        // Act
        ResponseEntity<?> response = authController.register(registerDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred", ((ErrorResponse) response.getBody()).getMessage());
    }

}
