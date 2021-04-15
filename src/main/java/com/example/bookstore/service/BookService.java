package com.example.bookstore.service;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.error.exception.ApiException;
import com.example.bookstore.error.exception.BookNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Publisher;
import com.example.bookstore.repository.BookRepository;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
@Value
public class BookService implements ResourceService<Book> {
  @Autowired
  AuthorService authorService;

  @Autowired
  PublisherService publisherService;

  @Autowired
  BookRepository bookRepository;

  @Autowired
  BookMapper bookMapper;

  public List<Book> getAll() {
    return bookRepository.findAllEagerLoad();
  }

  public Book find(Long id) throws BookNotFoundException {
    return bookRepository.findByIdEagerLoad(id).orElseThrow(() -> new BookNotFoundException(id));
  }

  public Book create(BookDto.Request bookDto) throws ApiException {
    Book book = bookMapper.map(bookDto);

    if (bookDto.getAuthorId() != null) {
      book.setAuthor(authorService.find(bookDto.getAuthorId()));
    }

    if (bookDto.getPublisherId() != null) {
      Publisher publisher = publisherService.find(bookDto.getPublisherId());
      book.setPublisher(publisher);
    }

    return save(book);
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
