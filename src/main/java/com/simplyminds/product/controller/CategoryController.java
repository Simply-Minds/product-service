package com.simplyminds.product.controller;

import com.simplyminds.api.CategoriesApi;
import com.simplyminds.model.*;
import com.simplyminds.product.service.CategoryService;
import com.simplyminds.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@RestController
@RequestMapping
@Validated
public class CategoryController implements CategoriesApi {

    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Create a new category in the catalog.
     *
     * @param category  the category details to be created
     * @return a response containing the created category details
     */
    @Override
    public ResponseEntity<CategoryResponseDTO> categoriesPost(Category category) {
        CategoryResponseDTO categoryResponseDTO = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponseDTO);
    }

    @Override
    public ResponseEntity<CategoryListResponseDTO> categoriesGet(Integer page, Integer size, String filter, String search) {
      CategoryListResponseDTO categoryListResponseDTO = categoryService.getListOfCategory(page,size,filter,search);
      return ResponseEntity.status(HttpStatus.OK).body(categoryListResponseDTO);
    }

    @Override
    public ResponseEntity<SuccessResponseDTO> categoriesIdDelete(Integer id) {
       SuccessResponseDTO successResponseDTO = categoryService.categoryIdDelete(id);
       return ResponseEntity.status(HttpStatus.OK).body(successResponseDTO);
    }

    @Override
    public ResponseEntity<CategoryResponseDTO> categoriesIdGet(Integer id) {
        CategoryResponseDTO categoryResponseDTO = categoryService.categoryIdGet(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTO);
    }

    @Override
    public ResponseEntity<CategoryResponseDTO> categoriesIdPut(Integer id, Category category) {
        CategoryResponseDTO categoryResponseDTO = categoryService.categoryIdPut(id,category);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTO);
    }

}
