package com.example.bookstore.controller;

import com.example.bookstore.Constants;
import com.example.bookstore.dto.entity.BookDto;
import com.example.bookstore.exception.ApiException;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.entity.Book;
import com.example.bookstore.service.AuthorService;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.PublisherService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.URL_PATH_PREFIX + "/books")
@AllArgsConstructor
public class BookController {
  @Autowired
  BookService bookService;

  @Autowired
  AuthorService authorService;

  @Autowired
  PublisherService publisherService;

  @Autowired
  BookMapper bookMapper;

  @GetMapping
  public List<BookDto.ResponseList> getAll() {
    return bookMapper.map(bookService.getAll());
  }

  @GetMapping("/{id}")
  public BookDto.Response find(@PathVariable("id") Long id) throws BookNotFoundException {
    return bookMapper.map(bookService.find(id));
  }

  @PostMapping
  public BookDto.Response create(@RequestBody BookDto.Request bookDto) throws ApiException {
    Book book = bookMapper.map(bookDto);

    if (bookDto.getAuthorId() != null) {
      book.setAuthor(authorService.find(bookDto.getAuthorId()));
    }

    if (bookDto.getPublisherId() != null) {
      book.setPublisher(publisherService.find(bookDto.getPublisherId()));
    }

    return bookMapper.map(bookService.save(book));
  }

  @PatchMapping("/{id}")
  public BookDto.Response update(@PathVariable("id") Long id, @RequestBody BookDto.Request bookDto)
          throws ApiException {
    Book book = bookService.find(id);
    book = bookService.save(bookMapper.updateFromDto(book, bookDto));
    return bookMapper.map(book);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") Long id) throws BookNotFoundException {
    bookService.delete(bookService.find(id));
  }
}
