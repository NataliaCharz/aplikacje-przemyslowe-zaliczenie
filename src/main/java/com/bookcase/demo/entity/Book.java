package com.bookcase.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table (name="BOOK")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="TITLE", nullable = false)
    private String title;

    @Column(name="PAGES")
    private Integer pages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="AUTHOR_ID", nullable = false)
    private Author author;
}
