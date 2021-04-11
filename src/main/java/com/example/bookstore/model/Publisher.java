package com.example.bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "publishers")
public class Publisher {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @NotBlank
  @Size(max = 50)
  private String name;

  @Column
  @NotBlank
  @Size(max = 100)
  private String address;

  @Column(name = "created_at", updatable = false)
  @CreationTimestamp
  private Date createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Date updatedAt;
}
