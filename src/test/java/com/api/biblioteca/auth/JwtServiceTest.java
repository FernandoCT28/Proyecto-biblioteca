package com.api.biblioteca.auth;

import com.api.biblioteca.config.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void testGetToken_Success() {
        UserDetails user = new User("user@example.com", "password", new ArrayList<>());

        String token = jwtService.getToken(user);

        assertNotNull(token);
    }

    @Test
    void testGetUsernameFromToken_Success() {
        UserDetails user = new User("user@example.com", "password", new ArrayList<>());
        String token = jwtService.getToken(user);

        String username = jwtService.getUsernameFromToken(token);

        assertEquals("user@example.com", username);
    }

    @Test
    void testIsTokenValid_Success() {
        UserDetails user = new User("user@example.com", "password", new ArrayList<>());
        String token = jwtService.getToken(user);

        boolean isValid = jwtService.isTokenValid(token, user);

        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired_False() {
        UserDetails user = new User("user@example.com", "password", new ArrayList<>());
        String token = jwtService.getToken(user);

        // Verificar que el token no haya expirado
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void testGetClaim_Success() {
        UserDetails user = new User("user@example.com", "password", new ArrayList<>());
        String token = jwtService.getToken(user);

        String username = jwtService.getClaim(token, Claims::getSubject);

        assertEquals("user@example.com", username);
    }

    @Test
    void testGetAllClaims_Success() {
        UserDetails user = new User("user@example.com", "password", new ArrayList<>());
        String token = jwtService.getToken(user);

        Claims claims = jwtService.getAllClaims(token);

        assertNotNull(claims);
        assertEquals("user@example.com", claims.getSubject());
    }

}
