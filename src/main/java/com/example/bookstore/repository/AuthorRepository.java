package com.example.bookstore.repository;

import com.example.bookstore.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
  @Query("SELECT CASE WHEN COUNT(a) > 0 THEN false ELSE true END " +
          "FROM Author a " +
          "WHERE a.email = :email AND a.id != :id")
  boolean isEmailUnique(@Param("email") String email, @Param("id") Long id);
}
