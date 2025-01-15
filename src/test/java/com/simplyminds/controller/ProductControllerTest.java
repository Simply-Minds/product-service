package com.simplyminds.controller;

import com.simplyminds.model.Product;
import com.simplyminds.model.ProductResponseDTO;
import com.simplyminds.product.controller.ProductController;
import com.simplyminds.product.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private Product product;
    private ProductResponseDTO productResponseDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setName("Test Product");
        product.setSku("SKU123");
        productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setData(product);
    }

    @Test
    void testCreateProduct() {
        Mockito.when(productService.createProduct(product)).thenReturn(productResponseDTO);
        ResponseEntity<ProductResponseDTO> responseEntity = productController.productsPost(product);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(productResponseDTO, responseEntity.getBody());
        Mockito.verify(productService).createProduct(product);
    }
}
