package com.simplyminds.product.service;

import com.simplyminds.model.Product;
import com.simplyminds.model.ProductListResponseDTO;
import com.simplyminds.model.ProductResponseDTO;

public interface ProductService {
    ProductResponseDTO createProduct(Product productDTO);

    ProductListResponseDTO getListOfProducts(Integer page, Integer size, String filter, String filterValue,String search);

}
