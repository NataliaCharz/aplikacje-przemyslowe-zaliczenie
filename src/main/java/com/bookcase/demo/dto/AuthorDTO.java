package com.bookcase.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {

    private Long id;
    @NotBlank(message = "Surname cannot be empty")
    @Size(min = 2, max = 100, message = "Surname must have length between 2 and 100 characters")
    private String surname;
    @NotNull(message = "Author status cannot be null")
    private AuthorStatus status;
    @NotNull(message = "Year of birth cannot be null")
    @Min(value = -3000, message = "Minimal year of birth is -3000")
    @Max(value = 2010, message = "Maximal year of birth is 2010")
    private Integer yearOfBirth;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<BookDTO> bookDTOS;
}
