package com.api.biblioteca.auth;

import com.api.biblioteca.service.auth.AuthService;
import com.api.biblioteca.config.JwtService;
import com.api.biblioteca.dto.auth.AuthDto;
import com.api.biblioteca.dto.auth.LoginDto;
import com.api.biblioteca.dto.auth.RegisterDto;
import com.api.biblioteca.entity.UserMongoEntity;
import com.api.biblioteca.repository.UserMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class AuthServiceTest {

    @Mock
    private UserMongoRepository userMongoRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        String username = "john@example.com";
        String password = "password";
        LoginDto loginDto = new LoginDto(username, password);

        UserMongoEntity user = new UserMongoEntity();
        user.setEmail(username);
        user.setPassword(password);

        when(userMongoRepository.findByEmail(username)).thenReturn(Optional.of(user));
        when(jwtService.getToken(any(UserMongoEntity.class))).thenReturn("mockToken");

        // Act
        AuthDto authDto = authService.login(loginDto);

        // Assert
        assertNotNull(authDto);
        assertEquals("mockToken", authDto.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testLoginUserNotFound() {
        // Arrange
        String username = "john@example.com";
        String password = "password";
        LoginDto loginDto = new LoginDto(username, password);

        when(userMongoRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act
        AuthDto authDto = authService.login(loginDto);

        // Assert
        assertNull(authDto);
        verify(authenticationManager, times(0)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testLoginAuthenticationFailure() {
        // Arrange
        String username = "john@example.com";
        String password = "password";
        LoginDto loginDto = new LoginDto(username, password);

        UserMongoEntity user = new UserMongoEntity();
        user.setEmail(username);
        user.setPassword(password);

        when(userMongoRepository.findByEmail(username)).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("Authentication error"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(loginDto));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testRegisterSuccess() {
        // Arrange
        String name = "John";
        String email = "john@example.com";
        String password = "password";
        RegisterDto registerDto = new RegisterDto(name, email, password);

        when(userMongoRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(jwtService.getToken(any(UserMongoEntity.class))).thenReturn("mockToken");

        // Act
        AuthDto authDto = authService.register(registerDto);

        // Assert
        assertNotNull(authDto);
        assertEquals("mockToken", authDto.getToken());
        verify(userMongoRepository, times(1)).save(any(UserMongoEntity.class));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        // Arrange
        String name = "John";
        String email = "john@example.com";
        String password = "password";
        RegisterDto registerDto = new RegisterDto(name, email, password);

        when(userMongoRepository.findByEmail(email)).thenReturn(Optional.of(new UserMongoEntity()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.register(registerDto));
        verify(userMongoRepository, times(0)).save(any(UserMongoEntity.class));
    }

    @Test
    void testRegisterInvalidPassword() {
        // Arrange
        String name = "John";
        String email = "john@example.com";
        String password = "123";
        RegisterDto registerDto = new RegisterDto(name, email, password);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.register(registerDto));
        verify(userMongoRepository, times(0)).save(any(UserMongoEntity.class));
    }

}
