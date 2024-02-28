package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    List<BookDto> findAll();

    BookDto getById(Long id);

    BookDto createBook(CreateBookRequestDto requestDto);

    void deleteById(Long id);

    BookDto updateById(CreateBookRequestDto requestDto, Long id);
}

