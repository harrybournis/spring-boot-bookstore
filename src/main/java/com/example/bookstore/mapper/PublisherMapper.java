package com.example.bookstore.mapper;

import com.example.bookstore.dto.model.PublisherDto;
import com.example.bookstore.model.Publisher;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Publisher map(PublisherDto publisherDto);

  PublisherDto map(Publisher publisher);

  List<PublisherDto> map(List<Publisher> publishers);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Publisher updateFromDto(@MappingTarget Publisher publisher, PublisherDto publisherDto);
}
