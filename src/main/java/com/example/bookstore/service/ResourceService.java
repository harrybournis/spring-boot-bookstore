package com.example.bookstore.service;

import com.example.bookstore.error.exception.ApiException;
import com.example.bookstore.error.exception.ResourceNotFoundException;

import java.util.List;

public interface ResourceService<T> {
  List<T> getAll();
  T find(Long id) throws ResourceNotFoundException;
  T save(T resource) throws ApiException;
  void delete(T resource);
}