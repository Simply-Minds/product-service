package com.simplyminds.product.service.impl;

import com.simplyminds.model.Product;
import com.simplyminds.model.ProductListResponseDTO;
import com.simplyminds.model.ProductListResponseDTOPagination;
import com.simplyminds.model.ProductResponseDTO;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.product.mapper.ProductMapper;
import com.simplyminds.product.service.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
public class ServiceHelperImpl implements ServiceHelper {
    @Autowired
    private ProductMapper productMapper;

    public ProductResponseDTO setProductResponseDTO(ProductEntity savedProduct, boolean success, String errCode, String errMessage){
        Product entityToProductDTO = productMapper.productEntityToProductDTO(savedProduct);
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setData(entityToProductDTO);
        productResponseDTO.errorCode(errCode);
        productResponseDTO.errorMessage(errMessage);
        productResponseDTO.setSuccess(success);
        return productResponseDTO;
    }
    public ProductListResponseDTO setListProductResponseDTO(boolean success, int page, int size, Page<Product> productsPage, String errCode, String errMessage){
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        productListResponseDTO.setData(productsPage.getContent());
        productListResponseDTO.errorMessage(errMessage);
        productListResponseDTO.errorCode(errCode);
        productListResponseDTO.setSuccess(success);
        ProductListResponseDTOPagination pagination = new ProductListResponseDTOPagination();
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


}