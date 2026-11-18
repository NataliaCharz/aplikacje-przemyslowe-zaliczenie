package com.bookcase.demo.entity;

import com.bookcase.demo.dto.AuthorStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Table(name="AUTHOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="SURNAME", nullable = false, length = 100)
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(name="STATUS", nullable = false)
    private AuthorStatus status;

    @Column(name="YEAR_OF_BIRTH", nullable = false)
    private Integer yearOfBirth;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "author")
    private List<Book> books;

}
