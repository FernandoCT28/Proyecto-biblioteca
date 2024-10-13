package com.api.biblioteca.repository;

import com.api.biblioteca.entity.ClientPostEntity;
import com.api.biblioteca.entity.UserMongoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientPostRepository extends JpaRepository<ClientPostEntity, Long> {

    Optional<ClientPostEntity> findByEmail(String email);

}
