package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.book.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify that correct BookDto was returned when calling createBook() method")
    public void createBook_WithValidRequestDto_ShouldReturnValidBookDto() {
        // Given
        Book book = createBook();
        BookDto bookDto = createBookDto(book);
        CreateBookRequestDto requestDto = createCreateBookRequestDto(book);

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        BookDto savedBookDto = bookService.createBook(requestDto);

        // Then
        assertThat(savedBookDto).isEqualTo(bookDto);
        verify(bookRepository, times(1)).save(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);

    }

    @Test
    @DisplayName("Verify that correct BookDto was returned after calling updateById() method")
    void updateBook_WithValidRequestDto_ShouldReturnValidBookDto() {
        //Given
        Book book = createBook();
        CreateBookRequestDto createBookRequestDto = createCreateBookRequestDto(book);
        createBookRequestDto.setDescription("Updated description");
        BookDto bookDto = createBookDto(book);
        bookDto.setDescription("Updated description");
        book.setDescription("Updated description");

        Long id = 1L;

        when(bookRepository.existsById(id)).thenReturn(true);
        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        //When
        BookDto actual = bookService.updateById(createBookRequestDto, id);

        //Then
        assertEquals(bookDto, actual);

        verify(bookRepository).save(book);
        verify(bookMapper).toModel(createBookRequestDto);
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);

    }

    @Test
    @DisplayName("Verify that updateById() method throws "
            + "EntityNotFoundException when book with given id does not exist")
    public void updateBook_WithNonExistingId_ShouldThrowEntityNotFoundException() {
        //Given
        Long invalidId = 2L;

        Book book = createBook();
        CreateBookRequestDto createBookRequestDto = createCreateBookRequestDto(book);
        createBookRequestDto.setTitle("Updated title");
        createBookRequestDto.setDescription("Updated description");

        when(!bookRepository.existsById(invalidId)).thenReturn(false);

        //When & Then
        assertThrows(EntityNotFoundException.class,
                () -> bookService.updateById(createBookRequestDto, invalidId));
    }

    @Test
    @DisplayName("Verify that deleteById() method deletes the book with the given id")
    void deleteBook_WithValidId_ShouldDeleteBook() {
        // Given
        Long testId = 1L;

        // When
        bookService.deleteById(testId);

        // Then
        verify(bookRepository, times(1)).deleteById(testId);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that findAllByCategoryId() method returns "
            + "correct BookDto when book with given id exists")
    public void findBookByCategoryId_WithValidBookId_ShouldReturnListOfBookDtoWithoutCategoryIds() {
        //Given
        Long categoryId = 1L;
        Book book = createBook();

        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = createBookDtoWithoutCategoryIds(book);

        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book))
                .thenReturn(bookDtoWithoutCategoryIds);

        //When
        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(categoryId);

        //Then
        assertEquals(List.of(bookDtoWithoutCategoryIds), actual);

        verify(bookRepository, times(1)).findAllByCategoryId(categoryId);
        verify(bookMapper, times(1)).toDtoWithoutCategories(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    private BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds(Book book) {
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setId(book.getId());
        bookDtoWithoutCategoryIds.setTitle(book.getTitle());
        bookDtoWithoutCategoryIds.setAuthor(book.getAuthor());
        bookDtoWithoutCategoryIds.setDescription(book.getDescription());
        bookDtoWithoutCategoryIds.setIsbn(book.getIsbn());
        bookDtoWithoutCategoryIds.setPrice(book.getPrice());
        bookDtoWithoutCategoryIds.setCoverImage(book.getCoverImage());
        return bookDtoWithoutCategoryIds;
    }

    private Book createBook() {
        Category testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setDescription("Test Description");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Title");
        book.setAuthor("Test Author");
        book.setDescription("Test Description");
        book.setIsbn("Test ISBN");
        book.setPrice(new BigDecimal("10.00"));
        book.setCoverImage("https://test.com/test.jpg");
        book.setCategories(Set.of(testCategory));
        book.setDeleted(false);
        return book;

    }

    private BookDto createBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setDescription(book.getDescription());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategoryIds(book.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toList()));
        return bookDto;
    }

    private CreateBookRequestDto createCreateBookRequestDto(Book book) {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setId(book.getId());
        createBookRequestDto.setTitle(book.getTitle());
        createBookRequestDto.setAuthor(book.getAuthor());
        createBookRequestDto.setDescription(book.getDescription());
        createBookRequestDto.setIsbn(book.getIsbn());
        createBookRequestDto.setPrice(book.getPrice());
        createBookRequestDto.setCoverImage(book.getCoverImage());
        createBookRequestDto.setCategories(book.getCategories());
        return createBookRequestDto;
    }

}
