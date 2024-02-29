package com.example.demo.service.book;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    List<BookDto> findAll(Pageable pageable);

    BookDto getById(Long id);

    BookDto createBook(CreateBookRequestDto requestDto);

    void deleteById(Long id);

    BookDto updateById(CreateBookRequestDto requestDto, Long id);
}

