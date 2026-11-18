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
    @NotBlank
    @Size(min = 2, max = 100)
    private String surname;
    @NotNull
    private AuthorStatus status;
    @NotNull
    @Min(-3000)
    @Max(2010)
    private Integer yearOfBirth;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<BookDTO> bookDTOS;
}
