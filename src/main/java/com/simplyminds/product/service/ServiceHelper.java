package com.simplyminds.product.service;


import com.simplyminds.model.Product;
import com.simplyminds.model.ProductListResponseDTO;
import com.simplyminds.model.ProductResponseDTO;
import com.simplyminds.product.entity.ProductEntity;
import org.springframework.data.domain.Page;



public interface ServiceHelper {

        ProductResponseDTO setProductResponseDTO(ProductEntity savedProduct, boolean success, String errCode, String errMessage);
     ProductListResponseDTO setListProductResponseDTO(boolean success, int page, int size, Page<Product> productsPage, String errCode, String errMessage);



}
