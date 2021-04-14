package com.example.bookstore.service;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.error.exception.ApiException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
@Value
public class BookService {
  @Autowired
  AuthorService authorService;

  @Autowired
  PublisherService publisherService;

  @Autowired
  BookRepository bookRepository;

  @Autowired
  BookMapper bookMapper;

  public List<Book> getAll() {
    return bookRepository.findAll();
  }

  public Book create(BookDto bookDto) throws ApiException {
    Book book = bookMapper.map(bookDto);

    if (bookDto.getAuthorId() != null) {
      book.setAuthor(authorService.find(bookDto.getAuthorId()));
    }

    if (bookDto.getPublisherId() != null) {
      book.setPublisher(publisherService.find(bookDto.getPublisherId()));
    }

    return save(book);
  }

  public Book save(@Valid Book book) throws ApiException {
    validateIsbnUniqueness(book.getIsbn(), book.getId());
    return bookRepository.save(book);
  }

  private void validateIsbnUniqueness(String isbn, Long id) throws ApiException {
    if (!bookRepository.isIsbnUnique(isbn, id != null ? id : 0L)) {
      throw new ApiException("book.isbn.must_be_unique", HttpStatus.BAD_REQUEST);
    }
  }
}
