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



@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductUnitRepository productUnitRepository;
    @Mock
    private ServiceHelper serviceHelper;

    @InjectMocks
    private ProductServiceImpl productService;
    private Product productDTO;
    private ProductEntity productEntity;
    private CategoryEntity categoryEntity;
    private ProductUnitEntity productUnitEntity;
    private ProductListResponseDTO productListResponseDTO;

    @BeforeEach
    void setUp() {
        productDTO = new Product();
        productDTO.setSku("SKU123");
        productDTO.setName("Test Product");
        productDTO.setPrice(100.0F);

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


        productListResponseDTO = new ProductListResponseDTO();
        productListResponseDTO.setSuccess(true);
        productListResponseDTO.setData(new ArrayList<>());

    }

    @Test
    void testCreateProduct_Success() {
        Mockito.when(productRepository.existsBySku(Mockito.any(String.class))).thenReturn(false);

        Mockito.when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(productEntity);
        Mockito.when(productMapper.productDTOToProductEntity(Mockito.any(Product.class)))
                .thenReturn(productEntity);

        ProductResponseDTO mockResponseDTO = new ProductResponseDTO();
        mockResponseDTO.setSuccess(true);
        mockResponseDTO.setData(productDTO);

        Mockito.when(serviceHelper.setProductResponseDTO(
                        Mockito.any(ProductEntity.class),
                        Mockito.anyBoolean(),
                        Mockito.any(),
                        Mockito.any()))
                .thenReturn(mockResponseDTO);

        ProductResponseDTO responseDTO = productService.createProduct(productDTO);

        Assertions.assertNotNull(responseDTO);
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

        ResourceAlreadyExistException exception = Assertions.assertThrows(ResourceAlreadyExistException.class, () -> {
            productService.createProduct(productDTO);
        });

        Assertions.assertEquals(ErrorCode.RES0001.getCode(), exception.getErrorCode());
        Assertions.assertEquals("SKU already exists.", exception.getMessage());
        Mockito.verify(productRepository, Mockito.times(1)).existsBySku(Mockito.any());
        Mockito.verify(productRepository, Mockito.times(0)).save(Mockito.any(ProductEntity.class));
    }
    // TESTS FOR productsIdDelete()
    @Test
    void testDeleteProduct_IdNotFound() {
        Integer wrongId = 1;
        Mockito.when(productRepository.findById(wrongId.longValue())).thenReturn(Optional.empty());

       NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            productService.productsIdDelete(wrongId);
        });

        Assertions.assertEquals(ErrorCode.ERR404.getCode(), exception.getErrorCode());

        Assertions.assertEquals("Product id not found.", exception.getMessage());
        Mockito.verify(productRepository, Mockito.times(0)).delete(Mockito.any(ProductEntity.class));
    }
    @Test
    void testDeleteProduct_IdExists() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        SuccessResponseDTO response = productService.productsIdDelete(1);

        Assertions.assertTrue(response.getSuccess());
        Mockito.verify(productRepository, Mockito.times(1)).delete(Mockito.any(ProductEntity.class));
    }

    // TESTS FOR productsPut()
    @Test
    void testUpdateProduct_Success() {
        Mockito.when(productRepository.existsBySku(Mockito.any(String.class))).thenReturn(false);
        Mockito.when(productMapper.productDTOToProductEntity(Mockito.any(Product.class))).thenReturn(productEntity);
        Mockito.when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(productEntity);
        //
        ProductResponseDTO mockResponseDTO = new ProductResponseDTO();
        mockResponseDTO.setSuccess(true);
        mockResponseDTO.setData(productDTO);

        Mockito.when(serviceHelper.setProductResponseDTO(
                        Mockito.any(ProductEntity.class),
                        Mockito.anyBoolean(),
                        Mockito.any(),
                        Mockito.any()))
                .thenReturn(mockResponseDTO);
        // use the service methode at the end instead of entity to dto
        productDTO.setName("jlss");
        ProductResponseDTO responseDTO = productService.productsIdPut(1,productDTO);

        Assertions.assertNotNull(responseDTO);

        Assertions.assertTrue(responseDTO.getSuccess());
        Assertions.assertEquals("SKU123", responseDTO.getData().getSku());
        Mockito.verify(productRepository,  Mockito.times(1)).existsBySku(Mockito.any());
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
        Assertions.assertEquals("SKU already exists.", exception.getMessage());
        Mockito.verify(productRepository, Mockito.times(1)).existsBySku(Mockito.any());
        Mockito.verify(productRepository, Mockito.times(0)).save(Mockito.any(ProductEntity.class));
    }

    // now create the test when the id is not valid for all methods that work on id
    @Test
    void testNullsFor_Update(){
        Integer invalidId = 0;

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            productService.productsIdPut(invalidId,productDTO);
        });

        Assertions.assertEquals(ErrorCode.BAD0001.getCode(), exception.getErrorCode());
        Assertions.assertEquals("Invalid id.", exception.getMessage());
        Mockito.verify(productRepository, Mockito.times(0)).existsBySku(Mockito.any());
        Mockito.verify(productRepository, Mockito.times(0)).save(Mockito.any(ProductEntity.class));
    }
    @Test
    void testNullsFor_Delete(){
        Integer invalidId = 0;

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            productService.productsIdDelete(invalidId);
        });

        Assertions.assertEquals(ErrorCode.BAD0001.getCode(), exception.getErrorCode());
        Assertions.assertEquals("Invalid id.", exception.getMessage());
        Mockito.verify(productRepository, Mockito.times(0)).existsBySku(Mockito.any());
        Mockito.verify(productRepository, Mockito.times(0)).delete(Mockito.any(ProductEntity.class));
    }

    // now test case for productsIdGet()
    @Test
    void testGetProduct_InvalidId(){
        Integer invalidId = 0;

        // now call methode on which operation is applying with that invalidValue and then check the response coming from the service methode
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class,() ->{
            productService.productsIdGet(invalidId);
        });
        // now check the response that it matches the expecting data, using Assertions
        Assertions.assertEquals(ErrorCode.BAD0001.getCode(),exception.getErrorCode());
        Assertions.assertEquals(exception.getMessage(),"Invalid id.");
        // now verify the methods that how much time they called vs expected
        Mockito.verify(productRepository,Mockito.times(0)).findById(Mockito.any());
        Mockito.verify(productMapper, Mockito.times(0)).productEntityToProductDTO(Mockito.any(ProductEntity.class));
    }
    @Test
    void testGetProduct_NotFound(){

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());
        // now call methode on which operation is applying with that invalidValue and then check the response coming from the service methode
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,() ->{
            productService.productsIdGet(1);
        });
        // now check the response that it matches the expecting data, using Assertions
        Assertions.assertEquals(ErrorCode.ERR404.getCode(),exception.getErrorCode());
        Assertions.assertEquals(exception.getMessage(),"Product not found.");
        // now verify the methods that how much time they called vs expected
        Mockito.verify(productRepository,Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(productMapper, Mockito.times(0)).productEntityToProductDTO(Mockito.any(ProductEntity.class));
    }
    @Test
    void testGetProduct_Success(){

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        ProductResponseDTO mockResponseDTO = new ProductResponseDTO();
        mockResponseDTO.setSuccess(true);
        mockResponseDTO.setData(productDTO);

        Mockito.when(serviceHelper.setProductResponseDTO(
                        Mockito.any(ProductEntity.class),
                        Mockito.anyBoolean(),
                        Mockito.any(),
                        Mockito.any()))
                .thenReturn(mockResponseDTO);
        ProductResponseDTO responseDTO = productService.productsIdGet(1);

        Assertions.assertNotNull(responseDTO);// checking that the response null
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
        Assertions.assertNotNull(responseDTO);
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
        Assertions.assertEquals(exception.getMessage(),"Not found.");
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

    // case 2. (default data) without filtering
//    @Test
//    void testGetProducts_Success(){
//// problem is ki apne ko db se data nahi lana he toh date set kerna padegaor ager data set kara toh fir pageble toh db se direct interect kerta he
//        // toh apan ager set bhi ker de tabh bhi kam nahi bane ga shayad
//        Mockito.when(productService.getListOfProducts(0,1,null,null,null)).thenReturn(productListResponseDTO);
//
//
//        ProductListResponseDTO productResponseDTO = productService.getListOfProducts(0,1,null,null,null);
//
//        Assertions.assertNotNull(productResponseDTO);// checking that the response is not null
//        Assertions.assertTrue(productResponseDTO.getSuccess()); // checking the getSuccess is true
//
//
//
//        // now verify the methods that how much time they called vs expected
//        Mockito.verify(productService,Mockito.times(1)).getDefaultProductList(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyBoolean());
//
//    }


}