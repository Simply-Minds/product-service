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
                return new ResponseDTO<>(true,products  , null, null);
            } else {
                throw new IllegalArgumentException("Either categoryId or categoryName must be provided.");
            }
        }catch (Exception ex){
            throw new RuntimeException("Internal server error: " + ex);
        }
    }

    @GetMapping("/products/filter/lowStock")
    public ResponseDTO<Object> getProductByCategory (
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
            return new ResponseDTO<>(true,products  , null, null);
        }catch (Exception ex){
            throw new RuntimeException("Internal server error: " + ex);
        }


    }
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping("/add")
    public Product addproduct(@RequestBody Product product){

        Category savedCategory = categoryRepository.save(product.getCategory());
        product.setCategory(savedCategory);
        return productRepository.save(product);
    }




}