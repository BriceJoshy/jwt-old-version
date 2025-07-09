package com.example.jwt_old_version.repository;

import com.example.jwt_old_version.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
