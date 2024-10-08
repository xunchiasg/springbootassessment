package com.example.demo.controller;

import com.example.demo.Service.BookService;
import com.example.demo.model.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/Book")

public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping
    public ResponseEntity allBooks(){
        return new  ResponseEntity<> (bookService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> saveBook(@RequestBody Book book){
        return new ResponseEntity<>(bookService.save(book), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable("id") Long bookId, @RequestBody Book book){

        Optional<Book> checkBook = BookService.findById(bookId).map(_book ->{
            _book.setId(book.getId());
            _book.setTitle(book.getTitle());
            _book.setAuthor(book.getAuthor());
            return bookService.save(_book);
        });

        if(checkBook.isEmpty())
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);


        return new ResponseEntity<>(checkBook, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook (@PathVariable("id") Long bookId){

        Optional<Book> checkBook = bookService.findById(bookIdId).map(_book->{
            bookService.deleteById(_book.getId());
            return _book;
        });
        if(checkBook.isEmpty())
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);

        String response = String.format("%s deleted successfully", checkBook.get().getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Object> countBook (){

        long count = bookService.count();

        if(count <= 0)
            return new ResponseEntity<>("No books found.", HttpStatus.NOT_FOUND);

        String response = String.format("Total books: %d", count);

        Map<String, Object> totalBooks = new HashMap<String, Object>();
        totalBooks.put("total", count);

        return new ResponseEntity<>(totalBooks, HttpStatus.OK);
    }



}
