package com.api.biblioteca.service.clients;

import com.api.biblioteca.dto.clients.ClientDto;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    ClientDto save(ClientDto user);
    Optional<ClientDto> getById(Long id);
    List<ClientDto> getAll();
    void delete(Long id);
    ClientDto update(ClientDto user, Long Id);

}
