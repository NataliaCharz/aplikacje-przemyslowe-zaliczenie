package com.bookcase.demo.service;

import com.bookcase.demo.dto.AuthorDTO;
import com.bookcase.demo.entity.Author;
import com.bookcase.demo.entity.Book;
import com.bookcase.demo.exception.AuthorNotFoundException;
import com.bookcase.demo.exception.BookNotFoundException;
import com.bookcase.demo.mapper.AuthorMapperMapStruct;
import com.bookcase.demo.dto.BookDTO;
import com.bookcase.demo.mapper.BookMapper;
import com.bookcase.demo.repository.AuthorRepository;
import com.bookcase.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    @Qualifier("authorMapperMapStruct")
    private final AuthorMapperMapStruct authorMapper;
    private final BookMapper bookMapper;

    public List<Book> getBooksService() {
        return this.bookRepository.findAll();
    }

    public Book getBookByIdService(Long id) {
        return this.bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public BookDTO createNewBookService(BookDTO bookDTO) {
        Book newBook = Book.builder()
                .title(bookDTO.getTitle())
                .pages(bookDTO.getPages())
                .author(this.authorService.getAuthorByIdService(bookDTO.getAuthorId()))
                .build();
        this.bookRepository.save(newBook);
        return bookMapper.mapBookToDto(newBook);

    }

    public BookDTO updateBookDTOService(Long id, BookDTO bookDTO) {
        Book bookToUpdate = getBookByIdService(id);
        bookMapper.mapDtoToBookInMemory(bookDTO, bookToUpdate);
        this.bookRepository.save(bookToUpdate);
        return bookMapper.mapBookToDto(bookToUpdate);
    }

    public void deleteBookByIdService(Long id) {
        Book book = this.getBookByIdService(id);
        this.bookRepository.delete(book);
    }
//
//    public List<Book> getBooksContainingCharactersService(String character) {
//        log.info("Received character: {}", character);
//        return bookRepository.findAll()
//                .stream()
//                .filter(b -> b.getTitle().toLowerCase().contains(character))
//                .collect(Collectors.toList());
//    }

//    public List<AuthorDTO> getBookAuthorService(String title) {
//        List<Book> bookList = this.bookRepository.findAllBooksByTitle(title);
//        List<Author> authorList = bookList.stream()
//                .map(Book::getAuthor)
//                .collect(Collectors.toList());
//        return authorMapper.mapAuthorToDTOList(authorList);
//    }
}
