package com.api.biblioteca.client;

import com.api.biblioteca.controller.clients.ClientController;
import com.api.biblioteca.dto.clients.ClientDto;
import com.api.biblioteca.exception.UserNotFoundException;
import com.api.biblioteca.service.clients.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class ClientControllerTest {
    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<ClientDto> mockClients = Arrays.asList(
                new ClientDto(1L, "John", "Doe", "john@example.com", "123456789"),
                new ClientDto(2L, "Jane", "Doe", "jane@example.com", "987654321")
        );

        when(clientService.getAll()).thenReturn(mockClients);

        ResponseEntity<List<ClientDto>> response = clientController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockClients.size(), response.getBody().size());
        verify(clientService, times(1)).getAll();
    }

    @Test
    void testGetUserById() {
        ClientDto mockClient = new ClientDto(1L, "John", "Doe", "john@example.com", "123456789");

        when(clientService.getById(1L)).thenReturn(Optional.of(mockClient));

        ResponseEntity<?> response = clientController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockClient, response.getBody());
        verify(clientService, times(1)).getById(1L);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(clientService.getById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = clientController.getUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientService, times(1)).getById(1L);
    }

    @Test
    void testCreateUser() {
        ClientDto mockClient = new ClientDto(1L, "John", "Doe", "john@example.com", "123456789");

        when(clientService.save(any(ClientDto.class))).thenReturn(mockClient);

        ResponseEntity<?> response = clientController.createUser(mockClient);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockClient, response.getBody());
        verify(clientService, times(1)).save(any(ClientDto.class));
    }

    @Test
    void testUpdateUser() {
        ClientDto mockClient = new ClientDto(1L, "John", "Doe", "john@example.com", "123456789");

        when(clientService.update(any(ClientDto.class), eq(1L))).thenReturn(mockClient);

        ResponseEntity<?> response = clientController.updateUser(1L, mockClient);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockClient, response.getBody());
        verify(clientService, times(1)).update(any(ClientDto.class), eq(1L));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(clientService).delete(1L);

        ResponseEntity<?> response = clientController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clientService, times(1)).delete(1L);
    }

    @Test
    void testDeleteUserNotFound() {
        doThrow(new UserNotFoundException(1L)).when(clientService).delete(1L);

        ResponseEntity<?> response = clientController.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientService, times(1)).delete(1L);
    }
}
