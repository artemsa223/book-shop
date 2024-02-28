package com.example.demo.dto;

import com.example.demo.validation.Url;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String author;
    private String isbn;
    @Min(0)
    private BigDecimal price;
    private String description;
    @Url
    private String coverImage;
}
