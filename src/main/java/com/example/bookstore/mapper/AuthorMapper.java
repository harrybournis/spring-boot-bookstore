package com.example.bookstore.mapper;

import com.example.bookstore.dto.model.AuthorDto;
import com.example.bookstore.model.Author;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AuthorMapper {
  @Mapping(target = "id", ignore = true)
  public abstract Author map(AuthorDto.Request authorDto);

  public abstract AuthorDto.Response map(Author author);

  public abstract List<AuthorDto.Response> map(List<Author> authors);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public abstract Author updateFromDto(@MappingTarget Author author, AuthorDto.Request authorDto);
}
