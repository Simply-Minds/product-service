package com.simplyminds.product.service.impl;

import com.simplyminds.model.*;
import com.simplyminds.product.entity.CategoryEntity;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.product.enums.ErrorCode;
import com.simplyminds.product.exception.BadRequestException;
import com.simplyminds.product.exception.ResourceAlreadyExistException;
import com.simplyminds.product.mapper.ProductMapper;
import com.simplyminds.product.repository.CategoryRepository;
import com.simplyminds.product.repository.ProductRepository;
import com.simplyminds.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;
    @Autowired
    CategoryRepository categoryRepository;


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

      List<ProductEntity> productEntities  = getListOfProductsByFilter(filter,filterValue);

        if (productEntities == null) {
            ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
            productListResponseDTO.setSuccess(Boolean.FALSE);
            productListResponseDTO.data(null);
            productListResponseDTO.errorCode("404");
            productListResponseDTO.errorMessage("not found body is empty");
            return productListResponseDTO;



        }
        System.out.println("called methode of pagination");
        return getPaginatedProducts(productEntities, page, size);
    }

    @Override
    public SuccessResponseDTO productsIdDelete(Integer id) {

        if (id<=0) {
            return null;
        }
        Optional<ProductEntity> product =  productRepository.findById(id.longValue());
        ProductEntity productEntity = product.get();

        if (productEntity == null) {
            return null;
        }
                productRepository.delete(productEntity);
              SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
              successResponseDTO.success(true);
              successResponseDTO.errorCode(null);
              successResponseDTO.errorMessage(null);
              successResponseDTO.data(null);
              return successResponseDTO;

    }

    @Override
    public ProductResponseDTO productsIdPut(Integer id, Product productDTO) {
        if (id == null) {
            return null;
        }
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

    @Override
    public ProductResponseDTO productsIdGet(Integer id) {
        if (id == null) {
            return null;

        }
        if(!(id<=0)){
                Optional<ProductEntity> product =  productRepository.findById(id.longValue());
                ProductEntity productEntity = product.get();
                Product entityToProductDTO = productMapper.productEntityToProductDTO(productEntity);
                ProductResponseDTO productResponseDTO = new ProductResponseDTO();
                productResponseDTO.data(entityToProductDTO);
                productResponseDTO.errorCode(null);
                productResponseDTO.errorMessage(null);
                productResponseDTO.setSuccess(true);
                return productResponseDTO;
        }


        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.data(null);
        productResponseDTO.errorCode("404");
        productResponseDTO.errorMessage("No product exists by id : "+ id);
        productResponseDTO.setSuccess(false);
        return productResponseDTO;

    }

    // filtering methode
    public List<ProductEntity>  getListOfProductsByFilter(String filter , String filterValue) {

        List<ProductEntity> filteredProducts = new ArrayList<>();
        // filter by category
        if(Objects.equals(filter, "category")){
            for (CategoryEntity categoryEntity : categoryRepository.findAll()) {
                System.out.println("category selected");
                System.out.println("category name : " + categoryEntity.getName());
                System.out.println(categoryEntity);
                System.out.println(
                        filterValue
                );
                if (filterValue.equalsIgnoreCase(categoryEntity.getName())) {
                    System.out.println("value"+categoryEntity.getName());
                    for (ProductEntity productEntity : productRepository.findAll()) {
                        if (Objects.equals(productEntity.getCategoryEntity().getCategoryId(), categoryEntity.getCategoryId())){
                            filteredProducts.add(productEntity);
                            System.out.println(productEntity.getCategoryEntity().getCategoryId());
                               System.out.println(productEntity);
                        }

                    }

                }

            }

        }
        else if (Objects.equals(filter, "lowStock")){
            System.out.println("lowstock");
            for (ProductEntity productEntity : productRepository.findAll()) {

                if (productEntity.getQuantityInStock() >= productEntity.getReorderLevel()) {

                    filteredProducts.add(productEntity);
                    System.out.println("lowstock found");

                }

            }
        }

        if (filteredProducts.isEmpty()) {

            return null;

        }
        System.out.println(
                "return filterlist"
        );
        return filteredProducts;

    }




    // pagination logic
    public ProductListResponseDTO getPaginatedProducts(List<ProductEntity> filteredProducts, int page, int size) {


        // PAGINATION logic
        // calculating essential variables
        Integer totalElements = filteredProducts.size();
        Integer totalPages = (int) Math.ceil((double) totalElements / size);
        Integer startIndex = page * size;
        Integer endIndex = Math.min(startIndex + size, totalElements);

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
        ProductListResponseDTOPagination pagination = new ProductListResponseDTOPagination();
        pagination.setTotalObjects(totalElements);
        pagination.currentPage(page);
        pagination.setTotalPages(totalPages);
        pagination.currentSize(size);
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        productListResponseDTO.setSuccess(Boolean.TRUE);
        productListResponseDTO.data(products);
        productListResponseDTO.errorCode(null);
        productListResponseDTO.errorMessage(null);
       productListResponseDTO.setPagination(pagination);
       productListResponseDTO.getPagination().getTotalObjects();
       productListResponseDTO.getPagination().getCurrentSize();
       productListResponseDTO.getPagination().getCurrentPage();
       productListResponseDTO.getPagination().getTotalPages();

        return productListResponseDTO;

    }

}

