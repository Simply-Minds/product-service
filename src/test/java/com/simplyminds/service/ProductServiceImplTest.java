package com.simplyminds.service;

import com.simplyminds.model.Product;
import com.simplyminds.model.ProductListResponseDTO;
import com.simplyminds.model.ProductResponseDTO;
import com.simplyminds.model.SuccessResponseDTO;
import com.simplyminds.product.entity.CategoryEntity;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.product.entity.ProductUnitEntity;
import com.simplyminds.product.enums.ErrorCode;
import com.simplyminds.product.exception.BadRequestException;
import com.simplyminds.product.exception.NotFoundException;
import com.simplyminds.product.exception.ResourceAlreadyExistException;
import com.simplyminds.product.mapper.ProductMapper;
import com.simplyminds.product.repository.CategoryRepository;
import com.simplyminds.product.repository.ProductRepository;
import com.simplyminds.product.repository.ProductUnitRepository;
import com.simplyminds.product.service.ProductService;
import com.simplyminds.product.service.impl.GenericServiceImpl;
import com.simplyminds.product.service.impl.ProductServiceImpl;
import com.simplyminds.product.service.ServiceHelper;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;


@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private ServiceHelper serviceHelper;

    private Product productDTO;
    private ProductEntity productEntity;
    private CategoryEntity categoryEntity;
    private ProductUnitEntity productUnitEntity;
    private ProductListResponseDTO productListResponseDTO;

    private SuccessResponseDTO successResponseDTO;
    private ProductResponseDTO productResponseDTO;

    @InjectMocks
    private ProductServiceImpl productService;



    @BeforeEach
    void setUp() {
        productDTO = new Product();
        productDTO.setSku("SKU123");
        productDTO.setName("Test Product");
        productDTO.setPrice(100.0F);
        productDTO.setId(1);

        categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(1L);
        categoryEntity.setName("electric");

        productUnitEntity = new ProductUnitEntity();
        productUnitEntity.setProductUnitId(1L);
        productUnitEntity.setUnitName("Kilogram");
        productUnitEntity.setUnitSpec("kg");

        productEntity = new ProductEntity();
        productEntity.setSku("SKU123");
        productEntity.setName("Test Product");
        productEntity.setPrice(BigDecimal.valueOf(100.0));
        productEntity.setCategoryEntity(categoryEntity);
        productEntity.setProductId(1L);// Now here we can assign it as default because the jpa will not work here

        productListResponseDTO = new ProductListResponseDTO();
        productListResponseDTO.setSuccess(true);
        productListResponseDTO.setData(new ArrayList<>());

        successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setSuccess(true);
        successResponseDTO.setData(null);
        successResponseDTO.setErrorCode(null);
        successResponseDTO.setErrorMessage(null);

        productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setSuccess(true);
        productResponseDTO.setData(productDTO);
        productResponseDTO.setErrorCode(null);
        productResponseDTO.setErrorMessage(null);

    }

    @Test
    void testCreateProduct_Success() {
        // Initialising the mocked data to be used in the actual service test
        Mockito.when(productRepository.existsBySku(Mockito.any(String.class))).thenReturn(false);

        Mockito.when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(productEntity);
        Mockito.when(productMapper.productDTOToProductEntity(Mockito.any(Product.class)))
                .thenReturn(productEntity);

        Mockito.when(serviceHelper.setProductResponseDTO(
                        Mockito.any(ProductEntity.class),
                        Mockito.anyBoolean(),
                        Mockito.any(),
                        Mockito.any()))
                .thenReturn(productResponseDTO);
        // testing the actual methode with dto and putting it into the responseDto to be checked later
        ProductResponseDTO responseDTO = productService.createProduct(productDTO);
// Assertions and verify methods to varify the response
        assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.getSuccess());
        Assertions.assertEquals("SKU123", responseDTO.getData().getSku());
        Mockito.verify(productRepository,  Mockito.times(1)).existsBySku(Mockito.any());
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(ProductEntity.class));
        Mockito.verify(productMapper, Mockito.times(1)).productDTOToProductEntity(Mockito.any(Product.class));
        Mockito.verify(serviceHelper, Mockito.times(1)).setProductResponseDTO(Mockito.any(ProductEntity.class), Mockito.anyBoolean(), Mockito.any(), Mockito.any());
    }
    @Test
    void testCreateProduct_SkuAlreadyExists() {
        Mockito.when(productRepository.existsBySku(Mockito.any(String.class))).thenReturn(true);

        // Q why this lambda expression , what else can we do here.
        ResourceAlreadyExistException exception = Assertions.assertThrows(ResourceAlreadyExistException.class, () -> {
            productService.createProduct(productDTO);
        });

        Assertions.assertEquals(ErrorCode.RES0001.getCode(), exception.getErrorCode());
        Assertions.assertEquals(ErrorCode.RES0001.getMessage(), exception.getMessage());
        Mockito.verify(productRepository, Mockito.times(1)).existsBySku(Mockito.any());
        Mockito.verify(productRepository, Mockito.times(0)).save(Mockito.any(ProductEntity.class));
    }
    // TESTS FOR productsIdDelete()
    @Test
    void testDeleteProduct_IdNotFound() {
        Integer wrongId = 1;
        Mockito.when(productRepository.existsById(wrongId.longValue())).thenReturn(false);

       NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            productService.productsIdDelete(wrongId);
        });

        Assertions.assertEquals(ErrorCode.ERR404.getCode(), exception.getErrorCode());

        Assertions.assertEquals(ErrorCode.ERR404.getMessage(), exception.getMessage());
        Mockito.verify(productRepository, Mockito.times(0)).delete(Mockito.any(ProductEntity.class));
    }
    @Test
    void testDeleteProduct_IdExists() {
        // Arrange: Mock repository to return true when checking if ID exists
        Integer productId = 1;
        Mockito.when(productRepository.existsById(productId.longValue())).thenReturn(true);

        Mockito.when(serviceHelper.setSuccessResponseDto(
                        Mockito.anyBoolean(),
                        Mockito.any(),
                        Mockito.any()))
                .thenReturn(successResponseDTO);
        // Act: Call the delete method
        SuccessResponseDTO response = productService.productsIdDelete(productId);

        // Assert: Ensure the response is not null and deletion was successful
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getSuccess());
          }
    // TESTS FOR productsPut()
    @Test
    void testUpdateProduct_Success() {
        // Mock repository to find the product by ID
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        // Ensure productEntity is not null when converting from DTO
        Mockito.when(productMapper.productDTOToProductEntity(Mockito.any(Product.class))).thenReturn(productEntity);

        // Ensure existsBySku returns false (no duplicate SKU)
        Mockito.when(productRepository.existsBySku(Mockito.any(String.class))).thenReturn(false);

        // Ensure save method returns a valid productEntity
        Mockito.when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(productEntity);




        Mockito.when(serviceHelper.setProductResponseDTO(
                        Mockito.any(ProductEntity.class),
                        Mockito.anyBoolean(),
                        Mockito.any(),
                        Mockito.any()))
                .thenReturn(productResponseDTO);

        // Act
        productDTO.setName("jlss");
        ProductResponseDTO responseDTO = productService.productsIdPut(1, productDTO);

        // Assert
        assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.getSuccess());
        Assertions.assertEquals("SKU123", responseDTO.getData().getSku());

        // Verify method calls
        Mockito.verify(productRepository, Mockito.times(1)).existsBySku(Mockito.any());
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(ProductEntity.class));
        Mockito.verify(productMapper, Mockito.times(1)).productDTOToProductEntity(Mockito.any(Product.class));
    }


    @Test
    void testUpdateProduct_SkuAlreadyExists() {
        Mockito.when(productRepository.existsBySku(Mockito.any(String.class))).thenReturn(true);

        ResourceAlreadyExistException exception = Assertions.assertThrows(ResourceAlreadyExistException.class, () -> {
            productService.productsIdPut(1,productDTO);
        });

        Assertions.assertEquals(ErrorCode.RES0001.getCode(), exception.getErrorCode());
        Assertions.assertEquals(ErrorCode.RES0001.getMessage(), exception.getMessage());
        Mockito.verify(productRepository, Mockito.times(1)).existsBySku(Mockito.any());
        Mockito.verify(productRepository, Mockito.times(0)).save(Mockito.any(ProductEntity.class));
    }

    // now create the test when the id is not valid for all methods that work on id
    @Test
    void testNullsFor_Update(){
        Integer invalidId = null;

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            productService.productsIdPut(invalidId,productDTO);
        });

        Assertions.assertEquals(ErrorCode.BAD0001.getCode(), exception.getErrorCode());
        Assertions.assertEquals(ErrorCode.BAD0001.getMessage(), exception.getMessage());
        Mockito.verify(productRepository, Mockito.times(0)).existsBySku(Mockito.any());
        Mockito.verify(productRepository, Mockito.times(0)).save(Mockito.any(ProductEntity.class));
    }

    @Test
    void testGetProduct_Success(){

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        Mockito.when(serviceHelper.setProductResponseDTO(
                        Mockito.any(ProductEntity.class),
                        Mockito.anyBoolean(),
                        Mockito.any(),
                        Mockito.any()))
                .thenReturn(productResponseDTO);
        ProductResponseDTO responseDTO = productService.productsIdGet(1);

        assertNotNull(responseDTO);// checking that the response null
        Assertions.assertTrue(responseDTO.getSuccess()); // checking the getSuccess is true
        Assertions.assertEquals("SKU123", responseDTO.getData().getSku());

        // now verify the methods that how much time they called vs expected
        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.any());

    }


    @Test
    void testGetListOfProducts_Success() {

        // directt create a page object which will contain the pageable data and used in the productRepository.findAll()
        // to get the data with the user entries
        // for now as it was an test case so we are supposing the otp  from the repo will be like productEntity object
        Page<ProductEntity> productPage = new PageImpl<>(List.of(productEntity));
        Mockito.when(productRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(productPage);

        // As we are using an service helper to give the response in ProductListResponseDto
        // so we need to ready the response too to check it further
        Mockito.when(serviceHelper.setListProductResponseDTO(
                Mockito.anyBoolean(),Mockito.anyInt(),Mockito.anyInt(),Mockito.any(),Mockito.any(),Mockito.any()
                )).thenReturn(productListResponseDTO);
        // Now finally after some useless steps we are here to call our actual code  with both filter as category and search as unit so that we can check our both the conditions simultaneously
        ProductListResponseDTO responseDTO = productService.getListOfProducts(0,1,"Category:electric","unit:kg");
        // now check the response that it as expected
        assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.getSuccess());
        // now verify that the methods that need to acctaully perform this actiosn were called or used or not so to do that
        // we use verify

        Mockito.verify(productRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));

    }


    // case 3. product Not found
    @Test
    void testGetProducts_NOTFound() {
        // directt create a page object which will contain the pageable data and used in the productRepository.findAll()
        // to get the data with the user entries
        // for now as it was an test case so we are supposing the otp  from the repo will be like productEntity object
        Page<ProductEntity> emptyPage = Page.empty();
        Mockito.when(productRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(emptyPage);


        // now call methode on which operation is applying with that invalidValue and then check the response coming from the service methode
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,() ->{
            productService.getListOfProducts(0,1,"category:xyz","name:Laptop");
        });
        // now check the response that it matches the expecting data, using Assertions
        Assertions.assertEquals(ErrorCode.ERR404.getCode(),exception.getErrorCode());
        Assertions.assertEquals(exception.getMessage(),ErrorCode.ERR404.getMessage());
        // now verify the methods that how much time they called vs expected

    }

    // this is my past thinkig on tha tlogic so i didt removed it fro here as it can help me later to understand the
    // the thinkig and learnoig process of human brain and how it approch
    // now the last test cases for the methode getProducts() and also for its child methods
    /**
     * We have 3 cases for getListOfProducts() :
     * 1. When search is not null . - call the getTheFilteredDataBySearch() and then call getListOfProductsByFilter() and then getPaginatedProducts() and return it.
     * /2\. When search and filter, filterValue are null . - call the getDefaultProductList() and return directly
     * 3. when products not found after applying the filters . - this will throw NotFoundException
     * 4. when filter and filterValue are present . - call this  getListOfProductsByFilter() firstly and then call getPaginatedProducts() and return it too
     * */


}