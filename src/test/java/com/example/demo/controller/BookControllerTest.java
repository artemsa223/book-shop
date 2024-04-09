package com.example.demo.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = {
            "classpath:database/book_category/delete-from-book_categories.sql",
            "classpath:database/category/delete-from-category.sql",
            "classpath:database/book/delete-from-books.sql",
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("create a new book")
    public void createBook_ValidRequestDto_Success() throws Exception {
        //Given
        Category category = new Category();
        category.setName("Test Category 1");
        category.setDescription("Test Cat. Description 1");
        Category savedCategory = categoryRepository.save(category);

        CreateBookRequestDto createBookRequestDto = getCreateBookRequestDto(savedCategory);

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle(createBookRequestDto.getTitle());
        bookDto.setAuthor(createBookRequestDto.getAuthor());
        bookDto.setIsbn(createBookRequestDto.getIsbn());
        bookDto.setPrice(createBookRequestDto.getPrice());
        bookDto.setDescription(createBookRequestDto.getDescription());
        bookDto.setCoverImage(createBookRequestDto.getCoverImage());
        bookDto.setCategoryIds(createBookRequestDto
                        .getCategories()
                        .stream()
                        .map(Category::getId)
                        .toList());

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);
        // When
        MvcResult result = mockMvc.perform(post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        //Then
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(bookDto, actual, "id");

    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = {
            "classpath:database/book/add-books-to-table.sql",
            "classpath:database/category/add-categories-to-table.sql",
            "classpath:database/book_category/add-book_categories-to-book_categories-table.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book_category/"
                    + "delete-from-book_categories.sql",
            "classpath:database/book/delete-from-books.sql",
            "classpath:database/category/delete-from-category.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("get all books")
    void getAll_GivenBooksInCatalog_ShouldReturnAllProducts() throws Exception {
        //Given
        List<BookDto> bookDtos = getBookDtos();

        //When
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(bookDtos, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    @Test
    @Sql(scripts = {
            "classpath:database/book/add-books-to-table.sql",
            "classpath:database/category/add-categories-to-table.sql",
            "classpath:database/book_category/"
                    + "add-book_categories-to-book_categories-table.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book_category/delete-from-book_categories.sql",
            "classpath:database/book/delete-from-books.sql",
            "classpath:database/category/delete-from-category.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("delete by id")
    void deleteById_GivenBookList() throws Exception {
        //Given
        List<BookDto> expected = getBookDtos();
        expected.remove(1);

        //When
        mockMvc.perform(delete("/books/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = {
            "classpath:database/book/add-specific-book-to-books-table.sql",
            "classpath:database/category/add-categories-to-table.sql",
            "classpath:database/book_category/"
                    + "add-category-to-specific-book-book_categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book_category/delete-from-book_categories.sql",
            "classpath:database/book/delete-from-books.sql",
            "classpath:database/category/delete-from-category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("get book by id")
    void getBookById_GivenSpecificBook_ShouldReturnCorrectBook() throws Exception {
        //Given
        BookDto bookDto = getBookDtos().get(0);

        //When
        MvcResult result = mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto.class);
        Assertions.assertEquals(bookDto, actual);
    }

    private CreateBookRequestDto getCreateBookRequestDto(Category savedCategory) {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("Test Book 1");
        createBookRequestDto.setAuthor("Test Author 1");
        createBookRequestDto.setIsbn("1234567890");
        createBookRequestDto.setPrice(new BigDecimal("10.00"));
        createBookRequestDto.setDescription("Test Book Description 1");
        createBookRequestDto.setCoverImage("test-cover-image-1");
        createBookRequestDto.setCategories(Set.of(savedCategory));
        return createBookRequestDto;
    }

    private List<BookDto> getBookDtos() {
        BookDto bookDto1 = new BookDto();
        bookDto1.setId(1L);
        bookDto1.setTitle("Book 1");
        bookDto1.setAuthor("Author 1");
        bookDto1.setIsbn("978055304");
        bookDto1.setPrice(BigDecimal.valueOf(29.99));
        bookDto1.setDescription("Description 1");
        bookDto1.setCoverImage("image1");
        bookDto1.setCategoryIds(List.of(1L));

        BookDto bookDto2 = new BookDto();
        bookDto2.setId(2L);
        bookDto2.setTitle("Book 2");
        bookDto2.setAuthor("Author 2");
        bookDto2.setIsbn("978055305");
        bookDto2.setPrice(BigDecimal.valueOf(22.22));
        bookDto2.setDescription("Description 2");
        bookDto2.setCoverImage("image2");
        bookDto2.setCategoryIds(List.of(1L));

        BookDto bookDto3 = new BookDto();
        bookDto3.setId(3L);
        bookDto3.setTitle("Book 3");
        bookDto3.setAuthor("Author 3");
        bookDto3.setIsbn("978055306");
        bookDto3.setPrice(BigDecimal.valueOf(30.11));
        bookDto3.setDescription("Description 3");
        bookDto3.setCoverImage("image3");
        bookDto3.setCategoryIds(List.of(1L));

        List<BookDto> bookDtos = new ArrayList<>();
        bookDtos.add(bookDto1);
        bookDtos.add(bookDto2);
        bookDtos.add(bookDto3);
        return bookDtos;
    }
}
