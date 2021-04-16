package com.example.bookstore.service;

import com.example.bookstore.exception.ApiException;
import com.example.bookstore.exception.AuthorNotFoundException;
import com.example.bookstore.model.Author;
import com.example.bookstore.repository.AuthorRepository;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
@Value
public class AuthorService implements ResourceService<Author> {
  @Autowired
  AuthorRepository authorRepository;

  public List<Author> getAll() {
    return authorRepository.findAll();
  }

  public Author find(Long id) throws AuthorNotFoundException {
    return authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
  }

  public Author save(@Valid Author author) throws ApiException {
    validateEmailUniqueness(author.getEmail(), author.getId());
    return authorRepository.save(author);
  }

  public void delete(Author author) {
    authorRepository.delete(author);
  }

  private void validateEmailUniqueness(String email, Long id) throws ApiException {
    if (!authorRepository.isEmailUnique(email, id != null ? id : 0L)) {
      throw new ApiException("author.email.must_be_unique", HttpStatus.BAD_REQUEST);
    }
  }
}
