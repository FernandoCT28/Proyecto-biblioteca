package com.api.biblioteca.repository;

import com.api.biblioteca.entity.BookPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksPostRepository extends JpaRepository<BookPostEntity, Long> {
    List<BookPostEntity> findByClientId(Long clientId);
}
