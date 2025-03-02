package com.simplyminds.product.service.impl;

import com.simplyminds.model.*;
import com.simplyminds.product.entity.CategoryEntity;
import com.simplyminds.product.enums.ErrorCode;
import com.simplyminds.product.exception.NotFoundException;
import com.simplyminds.product.exception.ResourceAlreadyExistException;
import com.simplyminds.product.mapper.CategoryMapper;
import com.simplyminds.product.repository.CategoryRepository;
import com.simplyminds.product.service.CategoryService;
import com.simplyminds.product.service.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends GenericServiceImpl<CategoryEntity, CategoryRepository> implements CategoryService {

    private final ServiceHelper serviceHelper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(
            @Qualifier("categoryRepository") CategoryRepository repository,
            CategoryMapper categoryMapper, // Injected properly
            ServiceHelper serviceHelper
    ) {
        super(repository);
        this.categoryMapper = categoryMapper; // Assigning the injected instance
        this.serviceHelper = serviceHelper;
    }

    @Override
    public CategoryResponseDTO createCategory(Category categoryDTO) {
        if (repository.existsByName(categoryDTO.getName())) {
            throw new ResourceAlreadyExistException(ErrorCode.RES0001.getCode(),ErrorCode.RES0001.getMessage());
        }

        CategoryEntity categoryEntity = categoryMapper.categoryDTOToCategoryEntity(categoryDTO);
        CategoryEntity savedProduct = super.createObject(categoryEntity);
        return serviceHelper.setCategoryResponseDTO(savedProduct,true,null,null);
    }

    @Override
    public CategoryListResponseDTO getListOfCategory(Integer page, Integer size, String filter, String search) {
        Page<CategoryEntity> categoryEntityPage = super.getListOfObjects(page,size,filter,search);
        return serviceHelper.setListCategoryResponseDTO(true,page,size,categoryEntityPage,null,null);
    }
    @Override
    public SuccessResponseDTO categoryIdDelete(Integer id) {
        return serviceHelper.setSuccessResponseDto(super.DeleteObject(id),null,null);
    }
    @Override
    public CategoryResponseDTO categoryIdPut(Integer id, Category categoryDTO) {
        if (repository.existsByName(categoryDTO.getName())) {
            throw new ResourceAlreadyExistException(ErrorCode.RES0001.getCode(),ErrorCode.RES0001.getMessage());
        }
        CategoryEntity categoryEntity = categoryMapper.categoryDTOToCategoryEntity(categoryDTO);
        CategoryEntity savedProduct = super.objectsIdPut(id,categoryEntity);
        return serviceHelper.setCategoryResponseDTO(savedProduct,true,null,null);
    }

    @Override
    public CategoryResponseDTO categoryIdGet(Integer id) {
      return serviceHelper.setCategoryResponseDTO(super.objectsIdGet(id),true,null,null);
    }
}
