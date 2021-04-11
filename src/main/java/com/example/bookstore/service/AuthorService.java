package com.example.bookstore.service;

import com.example.bookstore.error.ApiException;
import com.example.bookstore.model.Author;
import com.example.bookstore.repository.AuthorRepository;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@Value
public class AuthorService {
  @Autowired
  AuthorRepository authorRepository;

  public List<Author> getAll() {
    return authorRepository.findAll();
  }

  public Optional<Author> find(Long id) {
    return authorRepository.findById(id);
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
