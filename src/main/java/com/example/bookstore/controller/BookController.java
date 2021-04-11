package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.error.exception.ApiException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
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
  public List<BookDto> getAll() {
    return bookMapper.map(bookService.getAll());
  }

  @PostMapping
  public BookDto create(@RequestBody BookDto bookDto) throws ApiException {
    Book book = bookMapper.map(bookDto);
    book = bookService.save(book);
    return bookMapper.map(book);
  }
}
