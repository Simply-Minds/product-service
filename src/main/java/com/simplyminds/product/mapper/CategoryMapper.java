package com.simplyminds.product.mapper;

import com.simplyminds.model.Category;
import com.simplyminds.product.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Map a Category entity to a CategoryDto.
     *
     * @param categoryEntity the category entity
     * @return the corresponding CategoryDto
     */
    @Mapping(source = "categoryId", target = "id")
    Category categoryEntityToCategoryDTO(CategoryEntity categoryEntity);

    /**
     * Map a CategoryDto to a Category entity.
     *
     * @param categoryDto the category DTO
     * @return the corresponding Category entity
     */
    @Mapping(source = "id", target = "categoryId")
    CategoryEntity categoryDTOToCategoryEntity(Category categoryDto);

//    @Named("categoryIdToEntity")
//    default CategoryEntity categoryIdToEntity(Long categoryId) {
//        if (categoryId == null) {
//            return null;
//        }
//        CategoryEntity categoryEntity = new CategoryEntity();
//        categoryEntity.setCategoryId(categoryId);
//        return categoryEntity;
//    }
}
