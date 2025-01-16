package com.simplyminds.product.service.impl;

import com.simplyminds.model.Product;
import com.simplyminds.model.ProductListResponseDTO;
import com.simplyminds.model.ProductResponseDTO;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.product.enums.ErrorCode;
import com.simplyminds.product.exception.BadRequestException;
import com.simplyminds.product.exception.ResourceAlreadyExistException;
import com.simplyminds.product.mapper.ProductMapper;
import com.simplyminds.product.repository.ProductRepository;
import com.simplyminds.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (productRepository.existsBySku(productDTO.getSku())) {
            throw new ResourceAlreadyExistException(ErrorCode.RES0001.getCode(), "SKU already exists.");
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

    /**
     * Retrieves a List of products with pagination and sorting support.
     *
     * @param page     The no of page to retrieve.
     * @param size     The no of elements(objects) per page.
     * @param filter The filter to apply (find products by category).
     * @param filterValue    the value of filter to use like: category->food->chips or lowStock->no value needed
     * @return The list of products with filter applied if found; otherwise, returns response with error code, message,data=null.
     * @throws IllegalArgumentException if the @Param page and size, sortBy are invalid .
     * @throws RuntimeException         if error in server or an exceptional error
     * @since 1.0
     */

    // methode that will call another methode to do work
    @Override
    public ProductListResponseDTO getListOfProducts(Integer page, Integer size, String filter, String filterValue, String search) {

      List<ProductEntity> productEntities   = getListOfProductsByLowStock(filter,filterValue);
        if (productEntities == null) {
            return null;

        }
        return getPaginatedProducts(productEntities, page, size);
    }

    // filtering methode
    public List<ProductEntity>  getListOfProductsByLowStock(String filter , String filterValue) {

        List<ProductEntity> filteredProducts = new ArrayList<>();

        for (ProductEntity productEntity : productRepository.findAll()) {
            // filter by category
            if(Objects.equals(filter, "category")){
                if (Objects.equals(productEntity.getCategoryEntity().getName(), filterValue)) {

                    filteredProducts.add(productEntity);

                }
            }
            // filter by low stock
            else if (Objects.equals(filter, "lowStock")){
                if (productEntity.getQuantityInStock()<= productEntity.getReorderLevel()) {

                    filteredProducts.add(productEntity);

                }
            }

        }
        if (filteredProducts.isEmpty()) {
            return null;
        }
        return filteredProducts;

    }




    // pagination logic
    public ProductListResponseDTO getPaginatedProducts(List<ProductEntity> filteredProducts, int page, int size) {


        // PAGINATION logic
        // calculating essential variables
        int totalElements = filteredProducts.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        // return empty list if index is out of bound
        if (startIndex >= totalElements) {
            return new ProductListResponseDTO();
        }
        // creating sublist and then mapping the selected products to product dto
        List<ProductEntity> paginatedProducts = filteredProducts.subList(startIndex, endIndex);
        // now map all products to productDto list
        List<Product> products = new ArrayList<>();
        for (ProductEntity productEntity : paginatedProducts) {
            Product entityToProductDTO = productMapper.productEntityToProductDTO(productEntity);
            products.add(entityToProductDTO);
        }
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        productListResponseDTO.setSuccess(Boolean.TRUE);
        productListResponseDTO.data(products);
        productListResponseDTO.errorCode(null);
        productListResponseDTO.errorMessage(null);
        productListResponseDTO.getPagination().currentPage(page);
        productListResponseDTO.getPagination().currentSize(size);
        productListResponseDTO.getPagination().setTotalPages(totalPages);
        productListResponseDTO.getPagination().setTotalObjects(totalElements);
        return productListResponseDTO;

    }

}

