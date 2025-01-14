package com.simplyminds.product.Service;

import com.simplyminds.product.Model.PaginatedDTO;
import com.simplyminds.product.Model.Product;
import com.simplyminds.product.Model.ProductDTO;
import com.simplyminds.product.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The ProductService class handles all operations related to products,
 * including retrieving, adding, updating, and deleting products in the
 * inventory system.
 *
 * <p>This class interacts with the ProductRepository to access the database and
 * applies business logic before returning results to the calling service or controller.</p>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 *
 *      @Autowiered
 *      ProductRepository productRepo;
 *          Page<Product> productsPage = productRepo.findAll((org.springframework.data.domain.Pageable) pageable);
 * </pre>
 *
 * @author [jeet]
 * @version 1.0
 * @since 2025-01-14
 */

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    // methode for retrieving limited data
    public Page<Product> getProductsPage(int page, int size, String sortBy, boolean ascending) {

        // sorting by using sort class with two parameters sortBy,ascending
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);


        Page<Product> productsPage = productRepository.findAll((org.springframework.data.domain.Pageable) pageable);


        return productsPage;

    }



    // general methode for creating and returning productDTO objects instead of returning product
    public List<ProductDTO> getProductsDTO(List<Product> products) {

        List<ProductDTO> finalProducts = products.stream().map(product -> new ProductDTO(
                        product.getProductId(),
                        product.getCategory(),

                        product.getName(),
                        product.getPrice(),
                        product.getQuantityInStock()

                ))
                .toList();
        return finalProducts;


    }

    // filtered data by category
    public PaginatedDTO<List<ProductDTO>> getFilterListByCategory(String categoryId,int page, int size, String sortBy, boolean ascendin) {
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : productRepository.findAll()){
            if(Objects.equals(product.getCategory().getCategoryId(), categoryId)&&product!=null){

                filteredProducts.add(product);

            }

        }
        if (filteredProducts.isEmpty()) {
            return null;
        }
        return getPaginatedProducts(filteredProducts, page,size,sortBy,ascendin);
    }


    // filtered data by Low stock.
    public PaginatedDTO<List<ProductDTO>> getFilterListByLowStock(int page, int size, String sortBy, boolean ascendin) {
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : productRepository.findAll()){
            if(product.getQuantityInStock()<=product.getReorderLevel()&&product!=null){

                filteredProducts.add(product);

            }

        }
        if (filteredProducts.isEmpty()) {
            return null;
        }
        return getPaginatedProducts(filteredProducts, page,size,sortBy,ascendin);
    }

    // filtered data by supplier ------ will add when service layer is added


    //sorting the filtered list of products
    public PaginatedDTO<List<ProductDTO>> getPaginatedProducts(List<Product> filteredProducts, int page, int size, String sortBy, boolean ascending) {



        // cases of sorting
       if (Objects.equals(sortBy, "lowStock")) {
            // sorting based on Low Stock
            Comparator<Product> comparator = Comparator.comparing(product -> product.getQuantityInStock());
            if (ascending) {
                comparator = comparator.reversed();
            }
            filteredProducts.sort(comparator);
        } else if (Objects.equals(sortBy, "price")) {

            Comparator<Product> comparator = Comparator.comparing(product -> product.getPrice());
            if (ascending) {
                comparator = comparator.reversed();
            }
            filteredProducts.sort(comparator);
        } else if (Objects.equals(sortBy, "productId")) {

            Comparator<Product> comparator = Comparator.comparing(product -> product.getProductId());
            if (ascending) {
                comparator = comparator.reversed();
            }
            filteredProducts.sort(comparator);
        }

        // PAGINATION logic
        // calculating essential variables
        int totalElements = filteredProducts.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        // return empty list if index is out of bound
        if (startIndex >= totalElements) {
            return new PaginatedDTO<>(List.of(), totalElements, totalPages, page, size);
        }
        List<Product> paginatedProducts = filteredProducts.subList(startIndex, endIndex);
        // calling product dto
         List<ProductDTO> productDTOS  = getProductsDTO(paginatedProducts);

        return new PaginatedDTO<>(productDTOS, totalElements, totalPages, page, size);
    }



}









