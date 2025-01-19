package com.simplyminds.product.controller;

import com.simplyminds.api.ProductsApi;
import com.simplyminds.model.Product;
import com.simplyminds.model.ProductListResponseDTO;
import com.simplyminds.model.ProductResponseDTO;
import com.simplyminds.model.SuccessResponseDTO;
import com.simplyminds.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Validated
public class ProductController implements ProductsApi {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Create a new product in the catalog.
     *
     * @param product the product details to be created
     * @return a response containing the created product details
     */
    @Override
    public ResponseEntity<ProductResponseDTO> productsPost(Product product) {
        ProductResponseDTO productResponseDTO = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
    }

    @Override
    public ResponseEntity<ProductListResponseDTO> productsGet(Integer page, Integer size, String filter, String filterValue, String search) {
        ProductListResponseDTO products = productService.getListOfProducts(page, size, filter,filterValue,search);
        if (products == null) {
            return null;

        }
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @Override
    public ResponseEntity<SuccessResponseDTO> productsIdDelete(Integer id) {

        return ResponseEntity.status(HttpStatus.OK).body( productService.productsIdDelete(id));
    }


    @Override
    public ResponseEntity<ProductResponseDTO> productsIdPut(Integer id, Product product) {
      ProductResponseDTO productResponseDTO  = productService.productsIdPut(id, product);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }

    @Override
    public ResponseEntity<ProductResponseDTO> productsIdGet(Integer id) {
    ProductResponseDTO productResponseDTO = productService.productsIdGet(id);
    return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }


}
