package com.onurbas.service;

import com.onurbas.dto.CategoryDTO;
import com.onurbas.exception.BadRequestException;
import com.onurbas.exception.InternalServerErrorException;
import com.onurbas.exception.ResourceNotFoundException;
import com.onurbas.mapper.ICategoryMapper;
import com.onurbas.model.Category;
import com.onurbas.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final ICategoryRepository categoryRepository;

  public List<CategoryDTO> findAll() {
	try {
	  List<CategoryDTO> categoryDTOList = ICategoryMapper.INSTANCE.categoryListToCategoryDTOList(categoryRepository.findAll());
	  return categoryDTOList;
	} catch (Exception e) {
	  throw new InternalServerErrorException("An error occurred while fetching categories");
	}
  }

  public CategoryDTO findById(Long id) {
	if (id <= 0) {
	  throw new BadRequestException("Invalid category ID: " + id);
	}

	Optional<Category> categoryOptional = categoryRepository.findById(id);
	if (categoryOptional.isEmpty()) {
	  throw new ResourceNotFoundException("Category not found with ID: " + id);
	}
	CategoryDTO categoryDTO = ICategoryMapper.INSTANCE.categoryToCategoryDTO(categoryRepository.findById(id).get());
	return categoryDTO;
  }
  public Category getById(Long id) {
	if (id <= 0) {
	  throw new BadRequestException("Invalid category ID: " + id);
	}

	Optional<Category> categoryOptional = categoryRepository.findById(id);
	if (categoryOptional.isEmpty()) {
	  throw new ResourceNotFoundException("Category not found with ID: " + id);
	}

	return categoryOptional.get();
  }

  public CategoryDTO save(CategoryDTO categoryDTO) {
	try {
	  if (categoryDTO == null) {
		throw new BadRequestException("Category cannot be null");
	  }
	  Category savedCategory = categoryRepository.save(ICategoryMapper.INSTANCE.categoryDTOToCategory(categoryDTO));
	  CategoryDTO savedCategoryToCategoryDTO = ICategoryMapper.INSTANCE.categoryToCategoryDTO(savedCategory);
	  return savedCategoryToCategoryDTO;
	} catch (Exception e) {
	  throw new InternalServerErrorException("An error occurred while saving category");
	}
  }
  public CategoryDTO update(CategoryDTO categoryDTO,Long id){
	categoryDTO.setId(id);
	Category category = ICategoryMapper.INSTANCE.categoryDTOToCategory(categoryDTO);
	Category updatedCategory = categoryRepository.save(category);
	return ICategoryMapper.INSTANCE.categoryToCategoryDTO(updatedCategory);
  }
  public void deleteById(Long id) {
	Optional<Category> category = categoryRepository.findById(id);
	try {
	  if (category.isEmpty()) {
		throw new ResourceNotFoundException("Category not found with ID: " + id);
	  }
	  categoryRepository.deleteById(id);
	} catch (Exception e) {
	  throw new InternalServerErrorException("An error occurred while deleting category");
	}
  }
  public CategoryDTO findCategoryByCategoryNameIgnoreCase(String name) {
	Category category = categoryRepository.findCategoryByCategoryNameIgnoreCase(name);
	if (category == null) {
	  throw new ResourceNotFoundException("Category not found with name : " + name);
	}
	return ICategoryMapper.INSTANCE.categoryToCategoryDTO(category);
  }

}
