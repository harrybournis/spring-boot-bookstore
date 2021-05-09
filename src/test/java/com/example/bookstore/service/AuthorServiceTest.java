package com.example.bookstore.service;

import com.example.bookstore.UnitTest;
import com.example.bookstore.exception.ApiException;
import com.example.bookstore.exception.AuthorNotFoundException;
import com.example.bookstore.factories.AuthorFactory;
import com.example.bookstore.entity.Author;
import com.example.bookstore.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorServiceTest extends UnitTest {
  AuthorService authorService;

  @Autowired
  AuthorRepository authorRepository;

  AuthorService subject() {
    return new AuthorService(authorRepository);
  }

  @BeforeEach
  void clearDb() {
    authorRepository.deleteAll();
  }

  @Test
  @DisplayName("getAll with no records")
  void getAllNoRecords() {
    assertTrue(subject().getAll().isEmpty());
  }

  @Test
  @DisplayName("getAll returns authors")
  void getAllValid() {
    Author author = AuthorFactory.build();
    authorRepository.save(author);
    assertEquals(subject().getAll().get(0).getEmail(), author.getEmail());
  }

  @Test
  @DisplayName("find returns author when they exist")
  void findValid() throws AuthorNotFoundException {
    Author author = AuthorFactory.build();
    author = authorRepository.save(author);
    assertEquals(subject().find(author.getId()), author);
  }

  @Test
  @DisplayName("find throws AuthorNotFoundException")
  void findInvalid() {
    assertThrows(AuthorNotFoundException.class, () -> subject().find(0L));
  }

  @Test
  @DisplayName("save validates uniqueness and saves record")
  void save() throws ApiException {
    Author author = AuthorFactory.build();
    subject().save(author);
    assertEquals(authorRepository.count(), 1);
    Author authorFromDb = authorRepository.findAll().get(0);
    assertEquals(author.getEmail(), authorFromDb.getEmail());
  }

  @Test
  @DisplayName("save raises Exception if email exists")
  void saveInvalid() {
    String email = "email@aaa.com";
    authorRepository.save(AuthorFactory.builder().email(email).build());
    assertEquals(authorRepository.count(), 1);
    assertThrows(ApiException.class, () ->
            subject().save(AuthorFactory.builder().email(email).build())
    );
  }

  @Test
  @DisplayName("delete deletes author")
  void delete() {
    Author author = AuthorFactory.build();
    author = authorRepository.save(author);
    assertEquals(authorRepository.count(), 1);
    subject().delete(author);
    assertEquals(authorRepository.count(), 0);
  }
}