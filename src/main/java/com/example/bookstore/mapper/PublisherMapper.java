package com.example.bookstore.mapper;

import com.example.bookstore.dto.entity.PublisherDto;
import com.example.bookstore.entity.Publisher;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Publisher map(PublisherDto.Request publisherDto);

  PublisherDto.Response map(Publisher publisher);

  List<PublisherDto.Response> map(List<Publisher> publishers);

  PublisherDto.ResponseNested mapNested(Publisher publisher);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Publisher updateFromDto(@MappingTarget Publisher publisher, PublisherDto.Request publisherDto);
}
