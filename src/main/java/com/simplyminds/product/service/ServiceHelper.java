package com.simplyminds.product.service;


import com.simplyminds.model.*;
import com.simplyminds.product.entity.CategoryEntity;
import com.simplyminds.product.entity.ProductEntity;
import org.springframework.data.domain.Page;



public interface ServiceHelper {

    ProductResponseDTO setProductResponseDTO(ProductEntity savedProduct, boolean success, String errCode, String errMessage);
    ProductListResponseDTO setListProductResponseDTO(boolean success, int page, int size, Page<ProductEntity> productsPage, String errCode, String errMessage);
    CategoryResponseDTO setCategoryResponseDTO(CategoryEntity savedCategory, boolean success, String errCode, String errMessage);
    CategoryListResponseDTO setListCategoryResponseDTO(boolean success, int page, int size, Page<CategoryEntity> categoryPage, String errCode, String errMessage);
    SuccessResponseDTO setSuccessResponseDto(boolean success, String errCode, String errMessage);

}
