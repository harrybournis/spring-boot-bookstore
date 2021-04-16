package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
  String eagerLoadQuery = "join fetch b.author author join fetch b.publisher publisher";

  @Query("select b from Book b " + eagerLoadQuery + " order by author.lastName ASC, b.position ASC")
  List<Book> allVisiblePublishedSortedByPosition();

  @Query("select b from Book b " + eagerLoadQuery + " where b.id = :id")
  Optional<Book> findByIdEagerLoad(@Param("id") Long id);

  @Query("SELECT CASE WHEN COUNT(b) > 0 THEN false ELSE true END " +
          "FROM Book b " +
          "WHERE b.isbn = :isbn AND b.id != :id")
  boolean isIsbnUnique(@Param("isbn") String isbn, @Param("id") Long id);
}
