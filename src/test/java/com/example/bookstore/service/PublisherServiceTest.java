package com.example.bookstore.service;

import com.example.bookstore.UnitTest;
import com.example.bookstore.entity.Publisher;
import com.example.bookstore.exception.ApiException;
import com.example.bookstore.exception.PublisherNotFoundException;
import com.example.bookstore.factories.PublisherFactory;
import com.example.bookstore.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PublisherServiceTest extends UnitTest {
  PublisherService publisherService;

  @Autowired
  PublisherRepository publisherRepository;

  @BeforeEach
  void clearDb() {
    publisherRepository.deleteAll();
  }

  PublisherService subject() {
    return new PublisherService((publisherRepository));
  }

  @Test
  @DisplayName("getAll with no records")
  void getAllNoRecords() {
    publisherRepository.deleteAll();
    assertTrue(subject().getAll().isEmpty());
  }

  @Test
  @DisplayName("getAll returns publishers")
  void getAllValid() {
    Publisher publisher = PublisherFactory.build();
    publisherRepository.save(publisher);
    assertEquals(subject().getAll().get(0).getName(), publisher.getName());
  }

  @Test
  @DisplayName("find returns publisher when they exist")
  void findValid() throws PublisherNotFoundException {
    Publisher publisher = PublisherFactory.build();
    publisher = publisherRepository.save(publisher);
    assertEquals(subject().find(publisher.getId()), publisher);
  }

  @Test
  @DisplayName("find throws PublisherNotFoundException")
  void findInvalid() {
    assertThrows(PublisherNotFoundException.class, () -> subject().find(0L));
  }

  @Test
  @DisplayName("save validates uniqueness and saves record")
  void save() throws ApiException {
    Publisher publisher = PublisherFactory.build();
    subject().save(publisher);
    assertEquals(publisherRepository.count(), 1);
    Publisher publisherFromDb = publisherRepository.findAll().get(0);
    assertEquals(publisher.getName(), publisherFromDb.getName());
  }

  @Test
  @DisplayName("save raises Exception if name exists")
  void saveInvalid() {
    String name = "name";
    publisherRepository.save(PublisherFactory.builder().name(name).build());
    assertEquals(publisherRepository.count(), 1);
    assertThrows(ApiException.class, () ->
            subject().save(PublisherFactory.builder().name(name).build())
    );
  }

  @Test
  @DisplayName("delete deletes publisher")
  void delete() {
    Publisher publisher = PublisherFactory.build();
    publisher = publisherRepository.save(publisher);
    assertEquals(publisherRepository.count(), 1);
    subject().delete(publisher);
    assertEquals(publisherRepository.count(), 0);
  }
}