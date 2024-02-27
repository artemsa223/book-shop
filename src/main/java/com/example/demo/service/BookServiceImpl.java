package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found by id " + id)));
    }

    @Override
    public BookDto createBook(CreateBookRequestDto requestDto) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(requestDto)));
    }

    @Override
    public List<BookDto> findAllByTitle(String title) {
        return bookRepository.findAllByTitleContainsIgnoreCase(title)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateById(CreateBookRequestDto requestDto, Long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(requestDto.getTitle());
                    book.setAuthor(requestDto.getAuthor());
                    book.setIsbn(requestDto.getIsbn());
                    book.setPrice(requestDto.getPrice());
                    book.setDescription(requestDto.getDescription());
                    book.setCoverImage(requestDto.getCoverImage());
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new EntityNotFoundException("Book not found by id " + id)));
    }
}
