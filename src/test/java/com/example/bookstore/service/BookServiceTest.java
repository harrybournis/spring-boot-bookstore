package com.example.bookstore.service;

import com.example.bookstore.UnitTest;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Publisher;
import com.example.bookstore.exception.ApiException;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.factories.AuthorFactory;
import com.example.bookstore.factories.BookFactory;
import com.example.bookstore.factories.PublisherFactory;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookServiceTest extends UnitTest {
  BookService bookService;

  @Autowired
  BookRepository bookRepository;

  @Autowired
  AuthorRepository authorRepository;

  @Autowired
  PublisherRepository publisherRepository;

  @BeforeEach
  void clearDb() {
    bookRepository.deleteAll();
    authorRepository.deleteAll();
    publisherRepository.deleteAll();
  }

  BookService subject() {
    return new BookService(bookRepository);
  }

  Book buildBook() {
    Publisher publisher = PublisherFactory.build();
    Book book = BookFactory.builder().publisher(publisher).build();
    authorRepository.save(book.getAuthor());
    publisherRepository.save(book.getPublisher());
    return book;
  }

  @Test
  @DisplayName("getAll with no records")
  void getAllNoRecords() {
    bookRepository.deleteAll();
    assertTrue(subject().getAll().isEmpty());
  }

  @Test
  @DisplayName("getAll returns books with publishers sorted by author last name and position")
  void getAllValid() {
    Publisher publisher = PublisherFactory.builder().build();
    publisherRepository.save(publisher);
    Author author1 = AuthorFactory.builder().lastName("A").email("aa@dd.com").build();
    Author author2 = AuthorFactory.builder().lastName("B").email("bb@dd.com").build();
    authorRepository.save(author1);
    authorRepository.save(author2);

    Book book1 = BookFactory.builder().isbn("0-6645-1597-5").author(author2).publisher(publisher).position(2).build();
    book1 = bookRepository.save(book1);
    Book book2 = BookFactory.builder().isbn("0-7088-3058-7").author(author2).publisher(publisher).position(1).build();
    book2 = bookRepository.save(book2);
    Book book3 = BookFactory.builder().isbn("0-8592-5173-X").author(author1).publisher(publisher).position(1).build();
    book3 = bookRepository.save(book3);
    Book book4 = BookFactory.builder().isbn("0-6148-5147-5").author(author1).publisher(null).build();
    book4 = bookRepository.save(book4);

    assertEquals(bookRepository.count(), 4);

    List<Book> books = subject().getAll();
    assertEquals(3, books.size());
    assertEquals(books.get(0), book3);
    assertEquals(books.get(1), book2);
    assertEquals(books.get(2), book1);
  }

  @Test
  @DisplayName("find returns book when they exist")
  void findValid() throws BookNotFoundException {
    Book book = buildBook();
    book = bookRepository.save(book);
    assertEquals(subject().find(book.getId()), book);
  }

  @Test
  @DisplayName("find throws BookNotFoundException")
  void findInvalid() {
    assertThrows(BookNotFoundException.class, () -> subject().find(0L));
  }

  @Test
  @DisplayName("save validates uniqueness and saves record")
  void save() throws ApiException {
    Book book = buildBook();
    subject().save(book);
    assertEquals(bookRepository.count(), 1);
    Book bookFromDb = bookRepository.findAll().get(0);
    assertEquals(book.getIsbn(), bookFromDb.getIsbn());
  }

  @Test
  @DisplayName("save raises Exception if name exists")
  void saveInvalid() {
    Book book = buildBook();
    String isbn = book.getIsbn();
    bookRepository.save(book);
    assertEquals(bookRepository.count(), 1);
    assertThrows(ApiException.class, () ->
            subject().save(BookFactory.builder().isbn(isbn).build())
    );
  }

  @Test
  @DisplayName("delete deletes book")
  void delete() {
    Book book = buildBook();
    bookRepository.save(book);
    assertEquals(bookRepository.count(), 1);
    subject().delete(book);
    assertEquals(bookRepository.count(), 0);
  }
}