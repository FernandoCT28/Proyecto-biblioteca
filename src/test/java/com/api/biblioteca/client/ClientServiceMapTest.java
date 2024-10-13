package com.api.biblioteca.client;

import com.api.biblioteca.dto.clients.ClientDto;
import com.api.biblioteca.entity.ClientPostEntity;
import com.api.biblioteca.exception.UserNotFoundException;
import com.api.biblioteca.repository.ClientPostRepository;
import com.api.biblioteca.service.clients.ClientServiceMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceMapTest {

    @Mock
    private ClientPostRepository clientRepository;

    @InjectMocks
    private ClientServiceMap clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        ClientPostEntity entity1 = new ClientPostEntity();
        entity1.setId(1L);
        ClientPostEntity entity2 = new ClientPostEntity();
        entity2.setId(2L);

        when(clientRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<ClientDto> clients = clientService.getAll();

        assertEquals(2, clients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        ClientPostEntity entity = new ClientPostEntity();
        entity.setId(1L);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<ClientDto> client = clientService.getById(1L);

        assertTrue(client.isPresent());
        assertEquals(1L, client.get().getId());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> clientService.getById(1L));
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        ClientDto dto = new ClientDto(1L, "John", "Doe", "john@example.com", "123456789");
        ClientPostEntity entity = new ClientPostEntity();
        entity.setId(1L);

        when(clientRepository.save(any(ClientPostEntity.class))).thenReturn(entity);
        when(clientRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        ClientDto savedClient = clientService.save(dto);

        assertEquals(1L, savedClient.getId());
        verify(clientRepository, times(1)).save(any(ClientPostEntity.class));
    }

    @Test
    void testSaveEmailAlreadyExists() {
        ClientDto dto = new ClientDto(1L, "John", "Doe", "john@example.com", "123456789");
        ClientPostEntity entity = new ClientPostEntity();
        entity.setId(1L);

        when(clientRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> clientService.save(dto));
        verify(clientRepository, never()).save(any(ClientPostEntity.class));
    }

    @Test
    void testUpdate() {
        // Datos de prueba
        ClientDto dto = new ClientDto(1L, "John", "Doe", "john@example.com", "123456789");
        ClientPostEntity entity = new ClientPostEntity();
        entity.setId(1L);
        entity.setName("Old Name");
        entity.setLastName("Old LastName");
        entity.setEmail("old@example.com");
        entity.setPhoneNumber("987654321");

        // Simula el comportamiento del repositorio
        when(clientRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(clientRepository.save(any(ClientPostEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Llama al método a probar
        ClientDto updatedClient = clientService.update(dto, 1L);

        // Verifica el resultado
        assertEquals("John", updatedClient.getName());
        assertEquals("Doe", updatedClient.getLast_name());
        assertEquals("john@example.com", updatedClient.getEmail());
        assertEquals("123456789", updatedClient.getPhoneNumber());

        // Verifica que los métodos del repositorio se llamaron correctamente
        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).save(any(ClientPostEntity.class));
    }
    @Test
    void testUpdateNotFound() {
        ClientDto dto = new ClientDto(1L, "John", "Doe", "john@example.com", "123456789");

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> clientService.update(dto, 1L));
        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, never()).save(any(ClientPostEntity.class));
    }

    @Test
    void testDelete() {
        ClientPostEntity entity = new ClientPostEntity();
        entity.setId(1L);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(entity));

        clientService.delete(1L);

        verify(clientRepository, times(1)).delete(entity);
    }

    @Test
    void testDeleteNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> clientService.delete(1L));
        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, never()).delete(any(ClientPostEntity.class));
    }

}
