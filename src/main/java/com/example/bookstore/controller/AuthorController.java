package com.example.bookstore.controller;

import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.error.exception.ApiException;
import com.example.bookstore.error.exception.AuthorNotFoundException;
import com.example.bookstore.mapper.AuthorMapper;
import com.example.bookstore.model.Author;
import com.example.bookstore.service.AuthorService;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("authors")
@Value
public class AuthorController {
  @Autowired
  AuthorService authorService;

  @Autowired
  AuthorMapper authorMapper;

  @GetMapping
  public List<AuthorDto> getAll() {
    return authorMapper.map(authorService.getAll());
  }

  @GetMapping("/{id}")
  public AuthorDto find(@PathVariable Long id) throws AuthorNotFoundException {
    Author author = findAuthor(id);
    return authorMapper.map(author);
  }

  @PostMapping
  public AuthorDto create(@RequestBody AuthorDto authorDto) throws ApiException {
    Author author = authorService.save(authorMapper.map(authorDto));
    return authorMapper.map(author);
  }

  @PatchMapping("/{id}")
  public AuthorDto update(@PathVariable Long id, @RequestBody AuthorDto newAuthor) throws ApiException {
    Author author = findAuthor(id);
    author = authorService.save(authorMapper.updateFromDto(author, newAuthor));
    return authorMapper.map(author);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) throws AuthorNotFoundException {
    authorService.delete(findAuthor(id));
  }

  private Author findAuthor(Long id) throws AuthorNotFoundException {
    return authorService.find(id).orElseThrow(() -> new AuthorNotFoundException(id));
  }
}
