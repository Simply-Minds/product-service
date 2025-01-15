package com.simplyminds.service;

import com.simplyminds.model.Product;
import com.simplyminds.model.ProductResponseDTO;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.product.enums.ErrorCode;
import com.simplyminds.product.exception.ResourceAlreadyExistException;
import com.simplyminds.product.mapper.ProductMapper;
import com.simplyminds.product.repository.ProductRepository;
import com.simplyminds.product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product productDTO;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        productDTO = new Product();
        productDTO.setSku("SKU123");
        productDTO.setName("Test Product");
        productDTO.setPrice(100.0F);

        productEntity = new ProductEntity();
        productEntity.setSku("SKU123");
        productEntity.setName("Test Product");
        productEntity.setPrice(BigDecimal.valueOf(100.0));
    }

    @Test
    void testCreateProduct_Success() {
        Mockito.when(productRepository.existsBySku(Mockito.any(String.class))).thenReturn(false);
        Mockito.when(productMapper.productDTOToProductEntity(Mockito.any(Product.class))).thenReturn(productEntity);
        Mockito.when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(productEntity);
        Mockito.when(productMapper.productEntityToProductDTO(Mockito.any(ProductEntity.class))).thenReturn(productDTO);

        ProductResponseDTO responseDTO = productService.createProduct(productDTO);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.getSuccess());
        Assertions.assertEquals("SKU123", responseDTO.getData().getSku());
        Mockito.verify(productRepository,  Mockito.times(1)).existsBySku(Mockito.any());
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(ProductEntity.class));
        Mockito.verify(productMapper, Mockito.times(1)).productDTOToProductEntity(Mockito.any(Product.class));
        Mockito.verify(productMapper, Mockito.times(1)).productEntityToProductDTO(Mockito.any(ProductEntity.class));
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
}