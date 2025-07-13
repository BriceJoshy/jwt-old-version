package com.example.jwt_old_version.service;

import com.example.jwt_old_version.entity.Book;
import com.example.jwt_old_version.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;


    public Book createBook(Book book){
        return bookRepository.save(book);
    }

    public Optional<Book> updateBook(Long bookId, Book updatedBook){
        Optional<Book> bookToUpdate = bookRepository.findById(bookId);
        if(bookToUpdate.isEmpty()){
            return Optional.empty();
        }
        bookToUpdate.get().setTitle(updatedBook.getTitle());
        bookToUpdate.get().setAuthor(updatedBook.getAuthor());
        bookToUpdate.get().setDescription(updatedBook.getDescription());
        return Optional.of(bookRepository.save(bookToUpdate.get()));
    }

    public boolean deleteBook(Long id){
        if(bookRepository.findById(id).isEmpty()){
            return false;
        }
        bookRepository.deleteById(id);
        return true;
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }


    public boolean borrowBook(Long id){
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()){
            if(book.get().isAvailability()) {
                book.get().setAvailability(false);
                return true;
            }
            return false;
        }
        return false;
    }


    public boolean returnBook(Long id){
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()){
            book.get().setAvailability(true);
            bookRepository.save(book.get());
//            System.out.println("Saved Book details" + bookRepository.findById(id).toString());
            return true;
        }
        return false;
    }
}
