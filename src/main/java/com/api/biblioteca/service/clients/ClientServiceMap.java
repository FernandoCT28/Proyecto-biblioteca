package com.api.biblioteca.service.clients;


import com.api.biblioteca.dto.clients.ClientDto;
import com.api.biblioteca.entity.ClientPostEntity;
import com.api.biblioteca.entity.UserMongoEntity;
import com.api.biblioteca.exception.UserNotFoundException;
import com.api.biblioteca.repository.ClientPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceMap implements ClientService {

    @Autowired
    private ClientPostRepository clientRepository;

    public List<ClientDto> getAll() {
        try {
            return this.clientRepository.findAll().stream()
                    .map(this::toDto)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch all clients", e);
        }
    }

    public Optional<ClientDto> getById(Long id) {
        try {
            ClientPostEntity entity = this.clientRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));
            return Optional.of(toDto(entity));
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch client by ID: " + id, e);
        }
    }

    public ClientDto save(ClientDto client) {
        validateClientDto(client);

        Optional<ClientPostEntity> existingUser = clientRepository.findByEmail(client.getEmail());

        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("email already exists");
        }

        try {
            ClientPostEntity entity = new ClientPostEntity();
            entity.setName(client.getName());
            entity.setLastName(client.getLast_name());
            entity.setEmail(client.getEmail());
            entity.setPhoneNumber(client.getPhoneNumber());
            ClientPostEntity entitySaved = this.clientRepository.save(entity);
            return toDto(entitySaved);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save client", e);
        }
    }

    public ClientDto update(ClientDto client, Long id) {
        validateClientDto(client);

        // Encuentra el cliente actual por ID
        ClientPostEntity currentEntity = this.clientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Solo realiza la validación del email si es diferente al actual
        if (!currentEntity.getEmail().equals(client.getEmail())) {
            Optional<ClientPostEntity> existingUser = clientRepository.findByEmail(client.getEmail());

            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("Email already exists");
            }
        }

        try {
            currentEntity.setName(client.getName());
            currentEntity.setLastName(client.getLast_name());
            currentEntity.setEmail(client.getEmail());
            currentEntity.setPhoneNumber(client.getPhoneNumber());
            ClientPostEntity entitySaved = this.clientRepository.save(currentEntity);
            return this.toDto(entitySaved);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update client with ID: " + id, e);
        }
    }

    public void delete(Long id) {
        try {
            ClientPostEntity entity = this.clientRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));
            this.clientRepository.delete(entity);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete client with ID: " + id, e);
        }
    }

    private void validateClientDto(ClientDto clientDto) {
        if (clientDto.getName() == null || clientDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (clientDto.getLast_name() == null || clientDto.getLast_name().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }

        if (clientDto.getEmail() == null || clientDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (clientDto.getPhoneNumber() == null || clientDto.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
    }



    private ClientDto toDto(ClientPostEntity entity){
        return new ClientDto(entity.getId(),entity.getName(), entity.getLastName(), entity.getEmail(), entity.getPhoneNumber());
    }

}
