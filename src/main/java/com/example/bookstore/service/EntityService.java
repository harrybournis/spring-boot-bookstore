package com.example.bookstore.service;

import java.util.List;

import com.example.bookstore.exception.ApiException;
import com.example.bookstore.exception.ResourceNotFoundException;

import jakarta.validation.Valid;

public interface EntityService<T> {
  List<T> getAll();
  T find(Long id) throws ResourceNotFoundException;
  T save(@Valid T resource) throws ApiException;
  void delete(T resource);

}
