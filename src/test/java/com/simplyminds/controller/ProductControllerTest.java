//package com.simplyminds.controller;
//
//import com.simplyminds.model.Product;
//import com.simplyminds.model.ProductListResponseDTO;
//import com.simplyminds.model.ProductResponseDTO;
//import com.simplyminds.model.SuccessResponseDTO;
//import com.simplyminds.product.controller.ProductController;
//import com.simplyminds.product.service.ProductService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.AdditionalAnswers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//class ProductControllerTest {
//
//    @InjectMocks
//    private ProductController productController;
//
//    @Mock
//    private ProductService productService;
//
//    private Product product;
//    private ProductResponseDTO productResponseDTO;
//    private SuccessResponseDTO successResponseDTO;
//    private ProductListResponseDTO productListResponseDTO;
//
//    @BeforeEach
//    void setUp() {
//        product = new Product();
//        product.setName("Test Product");
//        product.setSku("SKU123");
//        productResponseDTO = new ProductResponseDTO();
//        productResponseDTO.setData(product);
//    }
//
//    @Test
//    void testCreateProduct() {
//        Mockito.when(productService.createProduct(product)).thenReturn(productResponseDTO);
//        ResponseEntity<ProductResponseDTO> responseEntity = productController.productsPost(product);
//        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        Assertions.assertEquals(productResponseDTO, responseEntity.getBody());
//        Mockito.verify(productService).createProduct(product);
//    }
//
//    @Test
//    void testProductsIdDeleteForTrueCase(){
//        // test case for true case when the getSuccess() returns true .
//        // Which mean our methode going to return the .OK status.
//        successResponseDTO = new SuccessResponseDTO();
//        successResponseDTO.setSuccess(true);
//        Mockito.when(productService.productsIdDelete(1)).thenReturn(successResponseDTO);
//        ResponseEntity<SuccessResponseDTO> responseEntity = productController.productsIdDelete(1);
//        Assertions.assertEquals(true,responseEntity.getBody().getSuccess());
//        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
//        Assertions.assertEquals(successResponseDTO,responseEntity.getBody());
//        Mockito.verify(productService).productsIdDelete(1);
//    }
//
//    @Test
//    void testProductsIdDeleteForFalseCase(){
//        // test case for false case when the getSuccess() returns true .
//        // Which mean our methode going to return the .OK status.
//        successResponseDTO = new SuccessResponseDTO();
//        successResponseDTO.setSuccess(false);
//        Mockito.when(productService.productsIdDelete(1)).thenReturn(successResponseDTO);
//        ResponseEntity<SuccessResponseDTO> responseEntity = productController.productsIdDelete(1);
//        Assertions.assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
//        Assertions.assertEquals(false,responseEntity.getBody().getSuccess());
//        Assertions.assertEquals(successResponseDTO,responseEntity.getBody());
//        Mockito.verify(productService).productsIdDelete(1);
//    }
//
//    //test case for put methode and then for list of products and then get one product and then we will write test cases for service
//
//    @Test
//    void testProductsIdPut(){
//        Mockito.when(productService.productsIdPut(1,product)).thenReturn(productResponseDTO);
//        ResponseEntity<ProductResponseDTO> responseEntity = productController.productsIdPut(1,product);
//        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        Assertions.assertEquals(productResponseDTO, responseEntity.getBody());
//        Mockito.verify(productService).productsIdPut(1,product);
//    }
//    @Test
//    void testProductIdGet(){
//        Mockito.when(productService.productsIdGet(1)).thenReturn(productResponseDTO);
//        ResponseEntity<ProductResponseDTO> responseEntity = productController.productsIdGet(1);
//        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
//        Assertions.assertEquals(productResponseDTO,responseEntity.getBody());
//        Mockito.verify(productService).productsIdGet(1);
//    }
//
//    @Test
//    void testGetProducts_BySearch(){
//        Mockito.when(productService.getListOfProducts(0,1,null,"category:electric")).thenReturn(productListResponseDTO);
//        ResponseEntity<ProductListResponseDTO> responseEntity = productController.productsGet(0,1,null,"category:electric");
//        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
//        Assertions.assertEquals(productListResponseDTO,responseEntity.getBody());
//        Mockito.verify(productService).getListOfProducts(0,1,null,"category:electric");
//    }
//
//
//
//
//
//
//
//
//
//}
