package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
  String eagerLoadQuery = "join fetch b.author author left join fetch b.publisher publisher";

  @Query("select b from Book b " +
          "join fetch b.author author join fetch b.publisher publisher " +
          "order by author.lastName ASC, b.position ASC")
  List<Book> allVisiblePublishedSortedByPosition();

  @Query("select b from Book b " + eagerLoadQuery + " where b.id = :id")
  Optional<Book> findByIdEagerLoad(@Param("id") Long id);

  Optional<Book> findByIsbn(String isbn);
}
