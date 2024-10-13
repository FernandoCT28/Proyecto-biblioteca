package com.api.biblioteca.controller.clients;

import com.api.biblioteca.dto.clients.ClientDto;
import com.api.biblioteca.exception.ErrorResponse;
import com.api.biblioteca.exception.UserNotFoundException;
import com.api.biblioteca.service.clients.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;


    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllUsers() {
        try {
            List<ClientDto> clients = clientService.getAll();
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            ClientDto client = clientService.getById(id).orElse(null);
            if (client == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Client not found"));
            }
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An unexpected error occurred"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody ClientDto clientDto) {
        try {
            ClientDto savedClient = clientService.save(clientDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An unexpected error occurred"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody ClientDto clientDto) {
        try {
            ClientDto updatedClient = clientService.update(clientDto, id);
            return ResponseEntity.ok(updatedClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An unexpected error occurred"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            clientService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An unexpected error occurred"));
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }



}
