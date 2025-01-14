package com.simplyminds.product.service.impl;

import com.simplyminds.model.Product;
import com.simplyminds.model.ProductResponseDTO;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.product.exception.BadRequestException;
import com.simplyminds.product.mapper.ProductMapper;
import com.simplyminds.product.repository.ProductRepository;
import com.simplyminds.product.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;


    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Create a new product in the catalog.
     *
     * @param productDTO the product details to be created
     * @return the created product as a DTO
     * @throws BadRequestException if the SKU already exists
     */
    @Override
    public ProductResponseDTO createProduct(Product productDTO) {
        // Validate unique SKU
        // Todo: update sku check here later
        if (productRepository.existsById(Long.valueOf(productDTO.getId()))) {
            throw new BadRequestException("SKU already exists.");
        }
        // Map DTO to entity
        ProductEntity product = productMapper.productDTOToProductEntity(productDTO);
        // Save the product
        ProductEntity savedProduct = productRepository.save(product);
        // Map entity back to DTO
        Product entityToProductDTO = productMapper.productEntityToProductDTO(savedProduct);
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setData(entityToProductDTO);
        productResponseDTO.setSuccess(Boolean.TRUE);
        return productResponseDTO;
    }
}
