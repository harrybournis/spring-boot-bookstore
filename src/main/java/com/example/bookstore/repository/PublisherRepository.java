package com.example.bookstore.repository;

import com.example.bookstore.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
  @Query("SELECT CASE WHEN COUNT(p) > 0 THEN false ELSE true END " +
          "FROM Publisher p " +
          "WHERE p.name = :name AND p.id != :id")
  boolean isNameUnique(@Param("name") String name, @Param("id") Long id);
}
