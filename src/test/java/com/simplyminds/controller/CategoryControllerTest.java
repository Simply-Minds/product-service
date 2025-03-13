package com.simplyminds.controller;

import com.simplyminds.model.Category;
import com.simplyminds.model.CategoryListResponseDTO;
import com.simplyminds.model.CategoryResponseDTO;
import com.simplyminds.model.SuccessResponseDTO;
import com.simplyminds.product.controller.CategoryController;
import com.simplyminds.product.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    private Category category;
    private CategoryResponseDTO categoryResponseDTO;
    private SuccessResponseDTO successResponseDTO;
    private CategoryListResponseDTO categoryListResponseDTO;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("electric");
        categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setData(category);
    }

    @Test
    void testCreatCategory() {

        Mockito.when(categoryService.createCategory(Mockito.any(Category.class))).thenReturn(categoryResponseDTO);
        ResponseEntity<CategoryResponseDTO> responseEntity = categoryController.categoriesPost(category);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(categoryResponseDTO, responseEntity.getBody());
        Mockito.verify(categoryService).createCategory(Mockito.any(Category.class));
    }

    @Test
    void testProductsIdDeleteForTrueCase(){
        // test case for true case when the getSuccess() returns true .
        // Which mean our methode going to return the .OK status.
        successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setSuccess(true);
        Mockito.when(categoryService.categoryIdDelete(1)).thenReturn(successResponseDTO);
        ResponseEntity<SuccessResponseDTO> responseEntity = categoryController.categoriesIdDelete(1);
        Assertions.assertEquals(true,responseEntity.getBody().getSuccess());
        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        Assertions.assertEquals(successResponseDTO,responseEntity.getBody());
        Mockito.verify(categoryService).categoryIdDelete(1);
    }//test case for put methode and then for list of products and then get one product and then we will write test cases for service

    @Test
    void testProductsIdPut(){
        Mockito.when(categoryService.categoryIdPut(1,category)).thenReturn(categoryResponseDTO);
        ResponseEntity<CategoryResponseDTO> responseEntity = categoryController.categoriesIdPut(1,category);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(categoryResponseDTO, responseEntity.getBody());
        Mockito.verify(categoryService).categoryIdPut(1,category);
    }
    @Test
    void testProductIdGet(){
        Mockito.when(categoryService.categoryIdGet(1)).thenReturn(categoryResponseDTO);
        ResponseEntity<CategoryResponseDTO> responseEntity = categoryController.categoriesIdGet(1);
        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        Assertions.assertEquals(categoryResponseDTO,responseEntity.getBody());
        Mockito.verify(categoryService).categoryIdGet(1);
    }

    @Test
    void testGetProducts_BySearch(){
        Mockito.when(categoryService.getListOfCategory(0,1,null,"category:electric")).thenReturn(categoryListResponseDTO);
        ResponseEntity<CategoryListResponseDTO> responseEntity = categoryController.categoriesGet(0,1,null,"category:electric");
        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        Assertions.assertEquals(categoryListResponseDTO,responseEntity.getBody());
        Mockito.verify(categoryService).getListOfCategory(0,1,null,"category:electric");
    }


}
