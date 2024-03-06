package com.example.demo.repository;

import com.example.demo.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b JOIN b.categories a WHERE a.id = :categoryId")
    List<Book> findAllByCategoryId(Long categoryId);

}

