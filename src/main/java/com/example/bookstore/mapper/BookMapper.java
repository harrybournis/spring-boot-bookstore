package com.example.bookstore.mapper;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.model.Book;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public abstract class BookMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "author", ignore = true)
  @Mapping(target = "publisher", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "visible", defaultValue = "true")
  public abstract Book map(BookDto bookDto);

  @Mapping(target = "publisherId", ignore = true)
  @Mapping(target = "authorId", ignore = true)
  @Mapping(target = "authorDto", source = "author", qualifiedByName = "mapWithFullName")
  public abstract BookDto map(Book book);

  public abstract List<BookDto> map(List<Book> books);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public abstract Book updateFromDto(@MappingTarget Book book, BookDto bookDto);
}
