package com.example.jwt_old_version.controller;


import com.example.jwt_old_version.entity.Book;
import com.example.jwt_old_version.entity.User;
import com.example.jwt_old_version.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping()
    public ResponseEntity<Book> createBook(@RequestBody Book book){
        return ResponseEntity.ok(bookService.createBook(book));
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable Long bookId, @RequestBody Book updateBook){
        Optional<Book> result = bookService.updateBook(bookId, updateBook);
        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
        return ResponseEntity.ok(updateBook);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId){
        if(bookService.deleteBook(bookId)){
            return ResponseEntity.ok("Book Deleted");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete Failed");
    }

    @GetMapping()
    public ResponseEntity<List<Book>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping("/{bookId}/borrow")
    public ResponseEntity<String> borrowBook(@PathVariable Long bookId){
        if(bookService.borrowBook(bookId)){
             return ResponseEntity.ok("Book Borrow Successful");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Borrow Failed");
    }

    @PostMapping("/{bookId}/return")
    public ResponseEntity<String> returnBook(@PathVariable Long bookId){
        if(bookService.returnBook(bookId)){
            return ResponseEntity.ok("Book Return Successful");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Return Failed");
    }

}
