package com.simplyminds.product.Controller;

import com.simplyminds.product.Model.*;
import com.simplyminds.product.Repository.CategoryRepository;
import com.simplyminds.product.Repository.ProductRepository;
import com.simplyminds.product.Service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    /**
     * Retrieves a List of products with pagination and sorting support.
     *
     * @param page The no of page to retrieve.
     * @param size The no of elements(objects) per page.
     * @param sortBy to sort page by.
     * @param ascending The direction of objects [ascending or descending].
     * @return The list of products if found; otherwise, returns response with error code, message,data=null.
     * @throws IllegalArgumentException if the @Param page and size, sortBy are invalid .
     * @throws RuntimeException if error in server or an exceptional error
     * @since 1.0
     */


    // default data without any external filters
    @GetMapping("/products")
    public ResponseDTO<Object> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "1") int size,
                                              @RequestParam(defaultValue = "productId") String sortBy,
                                              @RequestParam(defaultValue = "true") boolean ascending)

    {
        try {

            if (page < 0 || size <= 0) {
                throw new IllegalArgumentException("Page and size must be greater than 0.");
            }
            if (sortBy==null) {
                throw new IllegalArgumentException("sortBy can not be null");
            }
            Page<Product> products =  productService.getProductsPage(page, size, sortBy, ascending);
            if (products == null) {
                return new ResponseDTO<>(false,null  , 404, "products not found");
            }
            return new ResponseDTO<>(true,products  , null, null);
        }
        catch (Exception ex){
            throw new RuntimeException("Internal server error " + ex);
        }

    }

    /**
     * Retrieves a List of products with pagination and sorting support.
     *@param categoryId The filter to apply (find products by category).
     * @param page The no of page to retrieve.
     * @param size The no of elements(objects) per page.
     * @param sortBy to sort page by.
     * @param ascending The direction of objects [ascending or descending].
     * @return The list of products with filter applied if found; otherwise, returns response with error code, message,data=null.
     * @throws IllegalArgumentException if the @Param page and size, sortBy are invalid .
     * @throws RuntimeException if error in server or an exceptional error
     * @since 1.0
     */


    @GetMapping("/products/filter/byCategory")
    public ResponseDTO<Object> getProducts(
            @RequestParam String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending)
    {

        try {
            if (page < 0 || size < 0) {
                throw new IllegalArgumentException("Page and size must be greater than 0.");
            }
            if (sortBy==null) {
                throw new IllegalArgumentException("sortBy can not be null");
            }
            if (categoryId != null) {
                System.out.print("category id : "+categoryId+","+page+","+size);
                    PaginatedDTO<List<ProductDTO>> products  = productService.getFilterListByCategory(categoryId, page, size, sortBy, ascending);
                if (products == null) {
                    return new ResponseDTO<>(false,null  , 404, "products not found");
                }
                return new ResponseDTO<>(true,products  , null, null);
            } else {
                throw new IllegalArgumentException("Either categoryId or categoryName must be provided.");
            }
        }catch (Exception ex){
            throw new RuntimeException("Internal server error: " + ex);
        }
    }

    /**
     * Retrieves a List of products with pagination and sorting support.
     * @param page The no of page to retrieve.
     * @param size The no of elements(objects) per page.
     * @param sortBy to sort page by.
     * @param ascending The direction of objects [ascending or descending].
     * @return The list of products with filter applied if found; otherwise, returns response with error code, message,data=null.
     * @throws IllegalArgumentException if the @Param page and size, sortBy are invalid .
     * @throws RuntimeException if error in server or an exceptional error
     * @since 1.0
     */

    @GetMapping("/products/filter/lowStock")
    public ResponseDTO<Object> getProductByLowStock (
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "1") int size,
    @RequestParam(defaultValue = "productId") String sortBy,
    @RequestParam(defaultValue = "true") boolean ascending)
    {
        try {

            if (page < 0 || size < 0) {
                throw new IllegalArgumentException("Page and size must be greater than 0.");
            }
            if (sortBy==null) {
                throw new IllegalArgumentException("sortBy can not be null");
            }
            PaginatedDTO<List<ProductDTO>> products  = productService.getFilterListByLowStock( page, size, sortBy, ascending);
            if (products == null) {
                return new ResponseDTO<>(false,null  , 404, "products not found");
            }
            return new ResponseDTO<>(true,products  , null, null);
        }catch (Exception ex){
            throw new RuntimeException("Internal server error: " + ex);
        }


    }

    /**
     * Update an existing product by productId
     * @param  product The product object to update
     * @return updated product, returns response with error code, message,data=null.
     * @throws IllegalArgumentException if the @Param product invalid or null .
     * @throws RuntimeException if error in server or an exceptional error
     * @since 1.0
     */

    @PutMapping("/products/update")
    public ResponseDTO<Object> updateProduct(@RequestBody Product product){
        try {

            if (product.getProductId() == null) {
                throw new IllegalArgumentException("Please specify which product to update");
            }
            Product productResponse = productService.updateProduct(product);
            if (productResponse == null) {
                return new ResponseDTO<>(false,null  , 404, "products not found");
            }
            return new ResponseDTO<>(true,productResponse  , null, null);
        }catch (Exception ex){
            throw new RuntimeException("Internal server error: " + ex);
        }
    }
//
//    @DeleteMapping("/products/delete")
//    public ResponseDTO<Object> deleteProduct(@RequestParam(required = true) Long productId){
//
//        // TODO : TAKE user password before deleting any product from db, also validated it first
//
//       try{
//           if (productId == null) {
//               throw new IllegalArgumentException("product id is not provided");
//           }
//
//       }
//
//    }


}