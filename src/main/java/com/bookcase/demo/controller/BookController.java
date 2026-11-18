package com.bookcase.demo.controller;

import com.bookcase.demo.dto.BookDTO;
import com.bookcase.demo.mapper.BookMapper;
import com.bookcase.demo.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping()
    public List<BookDTO> getBooks(){
        return this.bookMapper.mapBookToDtoList(this.bookService.getBooksService());
    }

    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable(name = "id") Long id) {
        return this.bookMapper.mapBookToDto(bookService.getBookByIdService(id));
    }

    @PostMapping()
    public ResponseEntity<BookDTO> addNewBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO newBook = this.bookService.createNewBookService(bookDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(newBook.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newBook);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO updateBook(@PathVariable("id") Long id, @Valid @RequestBody BookDTO bookDTO) {
        return bookService.updateBookDTOService(id, bookDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBookByIdService(id);
    }

//    @GetMapping("/search")
//    public List<BookDTO> getBooksContainingCharacters(@RequestParam String character) {
//        return bookService.getBooksContainingCharactersService(character)
//                .stream()
//                .map(bookMapper::mapBookToDto)
//                .collect(Collectors.toList());
//    }

//
//    @GetMapping("/author")
//    public List<AuthorDTO> getBookAuthor(@RequestParam String title) {
//        return this.bookService.getBookAuthorService(title);
//    }
//


}
