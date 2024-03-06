package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found by id " + id)));
    }

    @Override
    public CategoryDto createCategory(CategoryDto requestDto) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toModel(requestDto)));
    }

    @Override
    public CategoryDto updateById(Long id, CategoryDto requestDto) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category with id: " + id + " does not exist");
        }
        Category update = categoryMapper.toModel(requestDto);
        update.setId(id);
        return categoryMapper.toDto(categoryRepository.save(update));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
