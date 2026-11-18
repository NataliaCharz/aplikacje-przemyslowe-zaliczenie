package com.bookcase.demo.mapper;

import com.bookcase.demo.dto.AuthorDTO;
import com.bookcase.demo.entity.Author;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {BookMapper.class})
public interface AuthorMapperMapStruct {

    @Mapping(source = "books", target = "bookDTOS")
    AuthorDTO mapAuthorToDto(Author author);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    void mapDtoToAuthorInMemory(AuthorDTO authorDTO, @MappingTarget Author author);
    List<AuthorDTO> mapAuthorToDtoList(List<Author> authors);
    Author mapDtoToAuthor(AuthorDTO authorDTO);

}


