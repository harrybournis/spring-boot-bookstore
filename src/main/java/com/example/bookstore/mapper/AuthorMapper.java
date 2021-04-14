package com.example.bookstore.mapper;

import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.model.Author;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AuthorMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  public abstract Author map(AuthorDto authorDto);

  public abstract AuthorDto map(Author author);

  public abstract AuthorDto.WithFullName mapWithFullName(Author author);

  public abstract List<AuthorDto> map(List<Author> authors);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public abstract Author updateFromDto(@MappingTarget Author author, AuthorDto authorDto);

  private String getFullName(Author author) {
    return author.getFirstName() + " " + author.getLastName();
  }
}
