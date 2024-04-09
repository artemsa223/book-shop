package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.category.CategoryServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void findAll_AllOk_ShouldReturnCategoryList() {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setName("Test1");
        category.setDescription("Test1");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Test2");
        category2.setDescription("Test2");

        List<Category> categories = List.of(category, category2);

        CategoryDto categoryDto1 = new CategoryDto(1L, "Mock category 1", "Mock description 1");
        CategoryDto categoryDto2 = new CategoryDto(2L, "Mock category 2", "Mock description 2");

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);
        List<CategoryDto> categoryDtoList = List.of(categoryDto1, categoryDto2);

        //When
        List<CategoryDto> actual = categoryService.findAll();

        //Then
        assertEquals(categoryDtoList, actual);

        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(1)).toDto(category);
        verify(categoryMapper, times(1)).toDto(category2);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void getById_Ok_ShouldReturnCategoryDtoById() {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setName("Test1");
        category.setDescription("Test1");

        CategoryDto categoryDto = new CategoryDto(1L, "Mock category 1", "Mock description 1");

        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // When
        CategoryDto actual = categoryService.getById(1L);

        // Then
        assertEquals(categoryDto, actual);

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void createCategory_WithValidRequestDto_ShouldReturnCategoryDto() {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setName("Test1");
        category.setDescription("Test1");

        CategoryDto categoryDto = new CategoryDto(1L, "Mock category 1", "Mock description 1");

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Test category",
                "Test description"
        );

        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // When
        CategoryDto actual = categoryService.createCategory(requestDto);

        // Then
        assertEquals(categoryDto, actual);
        verify(categoryRepository, times(1)).save(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void deleteById_WithExistingId_ShouldDeleteCategory() {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setName("Test1");
        category.setDescription("Test1");

        // When
        categoryService.deleteById(1L);

        // Then
        verify(categoryRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void updateById_WithNonExistingId_ShouldThrowEntityException() {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Test category",
                "Test description"
        );
        Long nonExistingId = 500L;

        // When
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.updateById(nonExistingId, requestDto));

        // Then
        verify(categoryRepository, times(1)).existsById(500L);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }
}
