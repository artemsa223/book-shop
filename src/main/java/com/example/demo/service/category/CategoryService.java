package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryRequestDto;
import java.util.List;

public interface CategoryService {
    List findAll();

    CategoryDto getById(Long id);

    CategoryDto createCategory(CreateCategoryRequestDto requestDto);

    CategoryDto updateById(Long id, CreateCategoryRequestDto requestDto);

    void deleteById(Long id);

}
