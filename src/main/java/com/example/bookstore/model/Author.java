package com.example.bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@DynamicUpdate
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authors")
public class Author {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name")
  @NotBlank
  @Size(max = 50)
  private String firstName;

  @Column(name = "last_name")
  @NotBlank
  @Size(max = 50)
  private String lastName;

  @Column(unique = true)
  @NotBlank
  @Size(max = 50)
  @Email(message = "invalid")
  private String email;

  @Column(name = "date_of_birth")
  private Date dateOfBirth;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreationTimestamp
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  @UpdateTimestamp
  private Date updatedAt;
}
