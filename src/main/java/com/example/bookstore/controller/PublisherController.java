package com.example.bookstore.controller;

import com.example.bookstore.Constants;
import com.example.bookstore.dto.entity.PublisherDto;
import com.example.bookstore.exception.ApiException;
import com.example.bookstore.exception.PublisherNotFoundException;
import com.example.bookstore.mapper.PublisherMapper;
import com.example.bookstore.entity.Publisher;
import com.example.bookstore.service.PublisherService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.URL_PATH_PREFIX + "/publishers")
@AllArgsConstructor
public class PublisherController {
  @Autowired
  PublisherService publisherService;

  @Autowired
  PublisherMapper publisherMapper;

  @GetMapping
  public List<PublisherDto> getAll() {
    return publisherMapper.map(publisherService.getAll());
  }

  @GetMapping("/{id}")
  public PublisherDto find(@PathVariable Long id) throws PublisherNotFoundException {
    Publisher publisher = findPublisher(id);
    return publisherMapper.map(publisher);
  }

  @PostMapping
  public PublisherDto create(@RequestBody PublisherDto publisherDto) throws ApiException {
    Publisher publisher = publisherService.save(publisherMapper.map(publisherDto));
    return publisherMapper.map(publisher);
  }

  @PatchMapping("/{id}")
  public PublisherDto update(@PathVariable Long id, @RequestBody PublisherDto publisherDto) throws ApiException {
    Publisher publisher = findPublisher(id);
    publisher = publisherService.save(publisherMapper.updateFromDto(publisher, publisherDto));
    return publisherMapper.map(publisher);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) throws PublisherNotFoundException {
    publisherService.delete(findPublisher(id));
  }

  private Publisher findPublisher(Long id) throws PublisherNotFoundException {
    return publisherService.find(id);
  }
}
