package com.simplyminds.product.service.impl;

import com.simplyminds.model.*;
import com.simplyminds.product.entity.CategoryEntity;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.product.mapper.CategoryMapper;
import com.simplyminds.product.mapper.ProductMapper;
import com.simplyminds.product.service.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;


@Service
public class ServiceHelperImpl implements ServiceHelper {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    public ProductResponseDTO setProductResponseDTO(ProductEntity savedProduct, boolean success, String errCode, String errMessage){
        Product entityToProductDTO = productMapper.productEntityToProductDTO(savedProduct);
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setData(entityToProductDTO);
        productResponseDTO.errorCode(errCode);
        productResponseDTO.errorMessage(errMessage);
        productResponseDTO.setSuccess(success);
        return productResponseDTO;
    }
    public ProductListResponseDTO setListProductResponseDTO(boolean success, int page, int size, Page<ProductEntity> productsPage, String errCode, String errMessage){
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        // Iterating over productsPage.getContent to map the
        List<Product> products = new ArrayList<>();
        for (ProductEntity product: productsPage.getContent()){
         products.add(productMapper.productEntityToProductDTO(product));
       }
        productListResponseDTO.setData(products);
        productListResponseDTO.errorMessage(errMessage);
        productListResponseDTO.errorCode(errCode);
        productListResponseDTO.setSuccess(success);
        PaginationDTO pagination = new PaginationDTO();
        pagination.setTotalObjects(((int) productsPage.getTotalElements()));
        pagination.currentPage(page);
        pagination.setTotalPages(productsPage.getTotalPages());
        pagination.currentSize(size);
        productListResponseDTO.setPagination(pagination);
        productListResponseDTO.getPagination().getTotalObjects();
        productListResponseDTO.getPagination().getCurrentSize();
        productListResponseDTO.getPagination().getCurrentPage();
        productListResponseDTO.getPagination().getTotalPages();
        return productListResponseDTO;
    }

    @Override
    public CategoryResponseDTO setCategoryResponseDTO(CategoryEntity savedCategory, boolean success, String errCode, String errMessage) {
        Category entityToCategoryDTO = categoryMapper.categoryEntityToCategoryDTO(savedCategory);
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setData(entityToCategoryDTO);
        categoryResponseDTO.errorCode(errCode);
        categoryResponseDTO.errorMessage(errMessage);
        categoryResponseDTO.setSuccess(success);
        return categoryResponseDTO;
    }

    @Override
    public CategoryListResponseDTO setListCategoryResponseDTO(boolean success, int page, int size, Page<CategoryEntity> categoryPage, String errCode, String errMessage) {
        CategoryListResponseDTO categoryListResponseDTO = new CategoryListResponseDTO();
        // Iterating over productsPage.getContent to map the
        List<Category> categories = new ArrayList<>();
        for (CategoryEntity categoryEntity: categoryPage.getContent()){
            categories.add(categoryMapper.categoryEntityToCategoryDTO(categoryEntity));
        }
        categoryListResponseDTO.setData(categories);
        categoryListResponseDTO.errorMessage(errMessage);
        categoryListResponseDTO.errorCode(errCode);
        categoryListResponseDTO.setSuccess(success);
        PaginationDTO pagination = new PaginationDTO();
        pagination.setTotalObjects(((int) categoryPage.getTotalElements()));
        pagination.currentPage(page);
        pagination.setTotalPages(categoryPage.getTotalPages());
        pagination.currentSize(size);
        categoryListResponseDTO.setPagination(pagination);
        categoryListResponseDTO.getPagination().getTotalObjects();
        categoryListResponseDTO.getPagination().getCurrentSize();
        categoryListResponseDTO.getPagination().getCurrentPage();
        categoryListResponseDTO.getPagination().getTotalPages();
        return categoryListResponseDTO;
    }


    @Override
    public SuccessResponseDTO setSuccessResponseDto(boolean success,String errCode,String errMessage){
        SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.success(success);
        successResponseDTO.errorCode(errCode);
        successResponseDTO.errorMessage(errMessage);
        successResponseDTO.data(null);
        return successResponseDTO;
    }


}