package com.bookcase.demo.service;

import com.bookcase.demo.dto.AuthorDTO;
import com.bookcase.demo.dto.AuthorStatus;
import com.bookcase.demo.entity.Author;
import com.bookcase.demo.exception.AuthorNotFoundException;
import com.bookcase.demo.exception.InvalidAuthorStatusException;
import com.bookcase.demo.mapper.AuthorMapperMapStruct;
import com.bookcase.demo.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;
    @Qualifier("authorMapperMapStruct")
    private final AuthorMapperMapStruct authorMapper;
//    private final BookMapper bookMapper;

    public List<AuthorDTO> getAllAuthorsService() {
        return this.authorRepository.findAll().stream().map(authorMapper::mapAuthorToDto).collect(Collectors.toList());
    }

    public AuthorDTO getAuthorByIdService(Long id) throws AuthorNotFoundException {
        return this.authorRepository.findById(id).map(authorMapper::mapAuthorToDto)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public AuthorDTO createAuthorService(AuthorDTO authorDTO) {
        Author newAuthor = Author.builder()
                .surname(authorDTO.getSurname())
                .status(authorDTO.getStatus())
                .yearOfBirth(authorDTO.getYearOfBirth())
                .build();
        Author saved = this.authorRepository.save(newAuthor);
        return authorMapper.mapAuthorToDto(saved);
    }


    public AuthorDTO updateAuthorService(Long id, AuthorDTO authorDTO) {
        Author authorToUpdate = getAuthorEntity(id);
        if (authorToUpdate.getStatus() == AuthorStatus.DEAD && (authorDTO.getStatus() == AuthorStatus.ALIVE || authorDTO.getStatus() == AuthorStatus.RETIRED)) {
            throw new InvalidAuthorStatusException();
        };
        authorMapper.mapDtoToAuthorInMemory(authorDTO, authorToUpdate);
        this.authorRepository.save(authorToUpdate);
        return authorMapper.mapAuthorToDto(authorToUpdate);
    }

    public void deleteAuthorService(Long id) {
        Author authorToDelete = getAuthorEntity(id);
        this.authorRepository.delete(authorToDelete);
    }

    private Author getAuthorEntity(Long id){
        return this.authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }
//
//    public List<Author> getAuthorsWithBooksService() {
//        return this.authorRepository.findAllWithBooks();
//    }
//
//    public List<Author> getAuthorsContainingCharactersInSurnameService(String character) {
//        return this.authorRepository.findAll().stream()
//                .filter(author -> author.getSurname().toLowerCase().contains(character.toLowerCase()))
//                .collect(Collectors.toList());
//    }
//
//    public List<BookDTO> getBooksByAuthorSurnameService(String surname){
//        List<Author> authorList = this.authorRepository.findBySurnameContainingIgnoreCase(surname);
//        List<Book> bookList = authorList.stream()
//                .flatMap(author -> author.getBooks().stream())
//                .collect(Collectors.toList());
//        return bookMapper.mapBookToDtoList(bookList);
//    }
//
//    public List<Author> getAuthorsDeadOrALiveService(Boolean isALive){
//        List<Author> allAuthors = this.authorRepository.findAll();
//        return allAuthors.stream()
//                .filter(author -> author.getAlive().equals(isALive))
//                .toList();
//    }
//

//


//
//    @Transactional
//    public AuthorDTO partialUpdateAuthorService(Long id, AuthorDTO authorDTO) {
//        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
//        log.info("Author before update: {}", author);
//        authorMapperForPartialUpdates.map(authorDTO, author);
//        log.info("Author after update: {}", author);
//        return authorMapper.mapAuthorToDTO(author);
//    }
//
//    public List<Book> getBooksByAuthorIdService(Long authorId) {
//        return authorRepository.findByIdWithBooks(authorId)
//                .orElseThrow(() -> new AuthorNotFoundException(authorId))
//                .getBooks();
//    }
//
//    public Long getAuthorsIdBySurnameService(String surname) {
//        return authorRepository.findBySurnameIgnoreCase(surname)
//                .map(Author::getId)
//                .orElseThrow(() -> new AuthorNotFoundException("No author found with surname containing: " + surname));
//    }
}

