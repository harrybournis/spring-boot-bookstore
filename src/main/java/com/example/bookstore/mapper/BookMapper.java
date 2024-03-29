package com.example.bookstore.mapper;

import com.example.bookstore.dto.entity.BookDto;
import com.example.bookstore.entity.Book;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public abstract class BookMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "visible", defaultValue = "true")
  public abstract Book map(BookDto.Request bookDto);

  public abstract BookDto.Response map(Book book);

  public abstract List<BookDto.ResponseList> map(List<Book> books);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public abstract Book updateFromDto(@MappingTarget Book book, BookDto.Request bookDto);
}
