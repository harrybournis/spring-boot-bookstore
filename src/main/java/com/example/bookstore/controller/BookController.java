package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.error.exception.ApiException;
import com.example.bookstore.error.exception.BookNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
@Value
public class BookController {
  @Autowired
  BookService bookService;

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
    Book book = bookService.create(bookDto);
    return bookMapper.map(book);
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
