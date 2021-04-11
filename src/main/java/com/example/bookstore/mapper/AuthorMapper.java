package com.example.bookstore.mapper;

import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.model.Author;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Author map(AuthorDto authorDto);

  AuthorDto map(Author author);

  List<AuthorDto> map(List<Author> authors);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Author updateFromDto(@MappingTarget Author author, AuthorDto authorDto);
}
