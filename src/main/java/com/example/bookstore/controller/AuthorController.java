package com.example.bookstore.controller;

import com.example.bookstore.Constants;
import com.example.bookstore.dto.model.AuthorDto;
import com.example.bookstore.exception.ApiException;
import com.example.bookstore.exception.AuthorNotFoundException;
import com.example.bookstore.mapper.AuthorMapper;
import com.example.bookstore.model.Author;
import com.example.bookstore.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.URL_PATH_PREFIX + "/authors")
@AllArgsConstructor
public class AuthorController {
  @Autowired
  AuthorService authorService;

  @Autowired
  AuthorMapper authorMapper;

  @GetMapping
  public List<AuthorDto.Response> getAll() {
    return authorMapper.map(authorService.getAll());
  }

  @GetMapping("/{id}")
  public AuthorDto.Response find(@PathVariable Long id) throws AuthorNotFoundException {
    Author author = findAuthor(id);
    return authorMapper.map(author);
  }

  @PostMapping
  public AuthorDto.Response create(@RequestBody AuthorDto.Request authorDto) throws ApiException {
    Author author = authorService.save(authorMapper.map(authorDto));
    return authorMapper.map(author);
  }

  @PatchMapping("/{id}")
  public AuthorDto.Response update(@PathVariable Long id, @RequestBody AuthorDto.Request newAuthor) throws ApiException {
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
    return authorService.find(id);
  }
}
