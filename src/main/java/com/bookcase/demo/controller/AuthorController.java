package com.bookcase.demo.controller;

import com.bookcase.demo.dto.AuthorDTO;
import com.bookcase.demo.mapper.AuthorMapperMapStruct;
import com.bookcase.demo.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/authors")
public class AuthorController {

    private final AuthorService authorService;
    @Qualifier("authorMapperMapStruct")
    private final AuthorMapperMapStruct authorMapper;
//    private final BookMapper bookMapper;

    @GetMapping()
    public List<AuthorDTO> getAuthors(){
        return authorMapper.mapAuthorToDtoList(authorService.getAllAuthorsService());
    }

    @GetMapping("/{id}")
    public AuthorDTO getAuthorById(@PathVariable(name = "id") Long id) {
        return authorMapper.mapAuthorToDto(authorService.getAuthorByIdService(id));
    }

    @PostMapping()
    public ResponseEntity<AuthorDTO> addNewAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        AuthorDTO newAuthor = this.authorService.createAuthorService(authorDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newAuthor.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newAuthor);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDTO updateAuthorById(@PathVariable(name = "id") Long id, @Valid @RequestBody AuthorDTO authorDTO) {
        return this.authorService.updateAuthorService(id, authorDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthorById(@PathVariable(name = "id") Long id) {
        authorService.deleteAuthorService(id);
    }

//    @GetMapping("/books")
//    public List<AuthorDTO> getAuthorsWithBooks() {
//            return authorMapper.mapAuthorToDTOList(authorService.getAuthorsWithBooksService());
//    }
//
//    @GetMapping("/search/{surname}")
//    public Long getAuthorsIdBySurname(@PathVariable String surname){
//        return authorService.getAuthorsIdBySurnameService(surname);
//    }
//
//    @GetMapping("/get-books-by-author-id/{id}")
//    public List<BookDTO> getBooksByAuthorId(@PathVariable(name = "id") Long id){
//        return bookMapper.mapBookToDtoList(authorService.getBooksByAuthorIdService(id));
//    }
//
//    @GetMapping("/get-author-by-surname/{author}")
//    public List<BookDTO> getBooksByAuthorSurname(@PathVariable(name = "author")String surname){
//        return authorService.getBooksByAuthorSurnameService(surname);
//    }
//
//    @GetMapping("/search")
//    public List<AuthorDTO> getAuthorsContainingCharactersInSurname(@RequestParam(name = "character") String character){
//        return authorMapper.mapAuthorToDTOList(authorService.getAuthorsContainingCharactersInSurnameService(character));
//    }
//
//
//

//
//    @PatchMapping("/{id}")
//    public AuthorDTO partialUpdateAlive(@RequestParam(name="id") Long id, @RequestBody AuthorDTO authorDTO){
//        return authorService.partialUpdateAuthorService(id, authorDTO);
//    }
}

