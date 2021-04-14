package com.example.bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @NotBlank
  @Size(max = 20)
  private String isbn;

  @Column
  @NotBlank
  @Size(max = 100)
  private String title;

  @Column
  private String description;

  @Column
  @NotNull
  private Boolean visible;

  @Column
  private Integer position;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  @NotNull
  private Author author;

  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "publisher_id", nullable = true)
  private Publisher publisher;

  @Column(name = "created_at", updatable = false)
  @CreationTimestamp
  private Date createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Date updatedAt;
}
