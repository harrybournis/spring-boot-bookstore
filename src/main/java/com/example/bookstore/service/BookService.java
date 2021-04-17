package com.example.bookstore.service;

import com.example.bookstore.exception.ApiException;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
@AllArgsConstructor
public class BookService implements ResourceService<Book> {
  @Autowired
  BookRepository bookRepository;

  public List<Book> getAll() {
    return bookRepository.allVisiblePublishedSortedByPosition();
  }

  public Book find(Long id) throws BookNotFoundException {
    return bookRepository.findByIdEagerLoad(id).orElseThrow(() -> new BookNotFoundException(id));
  }

  public Book save(@Valid Book book) throws ApiException {
    validateIsbnUniqueness(book.getIsbn(), book.getId());
    return bookRepository.save(book);
  }

  @Override
  public void delete(Book book) {
    bookRepository.delete(book);
  }

  private void validateIsbnUniqueness(String isbn, Long id) throws ApiException {
    if (!bookRepository.isIsbnUnique(isbn, id != null ? id : 0L)) {
      throw new ApiException("book.isbn.must_be_unique", HttpStatus.BAD_REQUEST);
    }
  }
}
