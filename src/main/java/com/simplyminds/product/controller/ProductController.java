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

    /**
     * get list of products with filtering.
     *
     * @param page        The no of page to retrieve.
     * @param size        The no of elements(objects) per page.
     * @param filter      The filter to apply (find products by category).
     * @param filterValue the value of filter to use like: category->food->chips or lowStock->no value needed
     * @return a list  of filtered products with pagination support for both cases: 1.when filter is provided, 2. when filter is not provided
     */
    @Override
    public ResponseEntity<ProductListResponseDTO> productsGet(Integer page, Integer size, String filter, String filterValue, String search) {


        ProductListResponseDTO products = productService.getListOfProducts(page, size, filter,filterValue,search);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
    /**
     * delete a product from the inventory by product id.
     *
     * @param id the ID of product to be deleted
     * @return the SuccessResponseDTO with Success message; returns null if not found
     */
    @Override
    public ResponseEntity<SuccessResponseDTO> productsIdDelete(Integer id) {
        SuccessResponseDTO successResponseDTO = productService.productsIdDelete(id);

        if (!successResponseDTO.getSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(successResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(successResponseDTO);
    }

    /**
     * update or edit an existing product from the inventory by product id.
     *
     * @param id the ID of product to be updated
     * @return the ProductResponseDTO with updated details
     */
    @Override
    public ResponseEntity<ProductResponseDTO> productsIdPut(Integer id, Product product) {
      ProductResponseDTO productResponseDTO  = productService.productsIdPut(id, product);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }
    /**
     * get a product by product id.
     *
     * @param id the ID of product to be retrieved
     * @return the ProductResponseDTO with updated details
     */
    @Override
    public ResponseEntity<ProductResponseDTO> productsIdGet(Integer id) {
    ProductResponseDTO productResponseDTO = productService.productsIdGet(id);
    return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }

}
