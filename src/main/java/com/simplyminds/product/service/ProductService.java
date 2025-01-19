package com.simplyminds.product.service;

import com.simplyminds.model.Product;
import com.simplyminds.model.ProductListResponseDTO;
import com.simplyminds.model.ProductResponseDTO;
import com.simplyminds.model.SuccessResponseDTO;
import com.simplyminds.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ProductResponseDTO createProduct(Product productDTO);

    ProductListResponseDTO getListOfProducts(Integer page, Integer size, String filter, String filterValue,String search);
    SuccessResponseDTO productsIdDelete(Integer id);

    ProductResponseDTO productsIdPut(Integer id, Product product);

    ProductResponseDTO productsIdGet(Integer id);

    ProductListResponseDTO getDefaultProductList(Integer page, Integer size, String sortBy, boolean ascending);
}
