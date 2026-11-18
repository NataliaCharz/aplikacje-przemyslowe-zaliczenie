package com.bookcase.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    @NotBlank(message = "Title cannot be empty")
    @Size(min = 2, max = 500, message = "Title must have length between 2 and 500 characters")
    private String title;
    @NotNull(message = "Pages cannot be null")
    @Min(value = 10, message = "Book must have at least 10 pages")
    private Integer pages;
    private Long authorId;
}
