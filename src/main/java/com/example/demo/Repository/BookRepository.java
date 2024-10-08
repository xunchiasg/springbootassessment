package com.example.demo.Repository;

import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    void add(Book book1);

    void saveAll(List<Book> all);

    Object size();
}
