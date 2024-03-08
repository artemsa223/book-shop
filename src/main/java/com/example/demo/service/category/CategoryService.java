package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryDto;
import java.util.List;

public interface CategoryService {
    List findAll();

    CategoryDto getById(Long id);

    CategoryDto createCategory(CategoryDto requestDto);

    CategoryDto updateById(Long id, CategoryDto requestDto);

    void deleteById(Long id);

}
