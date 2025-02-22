package com.simplyminds.service;


import com.simplyminds.model.*;
import com.simplyminds.product.entity.CategoryEntity;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.product.enums.ErrorCode;
import com.simplyminds.product.exception.NotFoundException;
import com.simplyminds.product.exception.ResourceAlreadyExistException;
import com.simplyminds.product.mapper.CategoryMapper;
import com.simplyminds.product.repository.CategoryRepository;
import com.simplyminds.product.service.ServiceHelper;
import com.simplyminds.product.service.impl.CategoryServiceImpl;
import com.simplyminds.product.service.impl.GenericServiceImpl;
import org.hibernate.boot.model.source.spi.AssociationSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.ArrayList.*;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {


    @Mock
    private ServiceHelper serviceHelper;
    @Mock
    private CategoryMapper categoryMapper;

    // HERE THE DATA THAT WE WANT OR OBJECT THAT WE WANT IS WRITTEN NO NEED TO MOCK THEM OR INJECT JUST DECLARED

    private CategoryEntity categoryEntity;
    private SuccessResponseDTO successResponseDTO;
    private CategoryResponseDTO categoryResponseDTO;
    private CategoryListResponseDTO categoryListResponseDTO;
    private Category category;
    @Mock
    private CategoryRepository categoryRepository;


        @InjectMocks
        private CategoryServiceImpl categoryService;


    /**
     * Creating a BeforeEach Methode to initialize all the
     * Necessary Dto and entities .
     * For the use in the actual test methods , need that data.
     * */
    // TODO : Add them in separate methods to avoid Unnecessary data initialising or memory consumption.


    @BeforeEach
    void setUP(){
        //Now need to initialize the previously declared Instance variables(Can be a class or service too) for this methode

        categoryEntity = new CategoryEntity(1L,"Electric","ad");

        category = new Category();
        category.setName("Electric");
        category.setId(1);

        successResponseDTO = new SuccessResponseDTO();
        successResponseDTO.setErrorMessage(null);
        successResponseDTO.setData(null);
        successResponseDTO.setSuccess(true);

        categoryListResponseDTO = new CategoryListResponseDTO();
        List<Category> categoryList = new ArrayList<>();
        categoryListResponseDTO.setData(categoryList);
        categoryListResponseDTO.setSuccess(true);

        categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setData(category);
        categoryResponseDTO.setErrorCode(null);
        categoryResponseDTO.setErrorMessage(null);
        categoryResponseDTO.setSuccess(true);

        
    }


    // Now the test cases will come from here

    // Case 1. CreateCategory
    @Test
    void testCreateCategory_Success(){
        Mockito.when(categoryRepository.save(Mockito.any(CategoryEntity.class))).thenReturn(categoryEntity);
        Mockito.when(categoryMapper.categoryDTOToCategoryEntity(Mockito.any(Category.class))).thenReturn(categoryEntity);
        Mockito.when(serviceHelper.setCategoryResponseDTO(
                Mockito.any(CategoryEntity.class),
                Mockito.anyBoolean(),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(categoryResponseDTO);
        Mockito.when(categoryRepository.existsByName(Mockito.any())).thenReturn(false);
        CategoryResponseDTO responseDTO = categoryService.createCategory(category);

        Assertions.assertTrue(responseDTO.getSuccess());
        Assertions.assertNotNull(responseDTO);

        // Now verifying the methods or services or classes that used or called through_out the methode

        Mockito.verify(categoryMapper, Mockito.times(1)).categoryDTOToCategoryEntity(category);
        Mockito.verify(categoryRepository,Mockito.times(1)).save(categoryEntity);
        Mockito.verify(categoryRepository,Mockito.times(1)).existsByName(Mockito.any());
        Mockito.verify(serviceHelper,Mockito.times(1)).setCategoryResponseDTO(Mockito.any(CategoryEntity.class),Mockito.anyBoolean(),Mockito.any(),Mockito.any());

    }
    // test case for alreadyExistsName
    @Test
    void testCreateCategory_NameAlreadyExists(){
        Mockito.when(categoryRepository.existsByName(Mockito.any(String.class))).thenReturn(true);

        ResourceAlreadyExistException exception = Assertions.assertThrows(ResourceAlreadyExistException.class, () ->{
            categoryService.createCategory(category);
        });

        Assertions.assertEquals(exception.getMessage(), ErrorCode.RES0001.getMessage());
        Assertions.assertEquals(exception.getErrorCode(),ErrorCode.RES0001.getCode());

        Mockito.verify(categoryRepository,Mockito.times(1)).existsByName(Mockito.any());
        Mockito.verify(categoryRepository,Mockito.times(0)).save(Mockito.any(CategoryEntity.class));
    }
    // Case 2. categoryIdPut
    @Test
    void testUpdateCategory_Success(){
        Mockito.when(categoryMapper.categoryDTOToCategoryEntity(Mockito.any(Category.class))).thenReturn(categoryEntity);
        Mockito.when(categoryRepository.existsByName(Mockito.any(String.class))).thenReturn(false);
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        Mockito.when(serviceHelper.setCategoryResponseDTO(Mockito.any(CategoryEntity.class),Mockito.anyBoolean(),Mockito.any(),Mockito.any())).thenReturn(categoryResponseDTO);
        Mockito.when(categoryRepository.save(Mockito.any(CategoryEntity.class))).thenReturn(categoryEntity);
        category.setName("JLSS");
        CategoryResponseDTO responseDTO = categoryService.categoryIdPut(1,category);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.getSuccess());

        Mockito.verify(categoryRepository,Mockito.times(1)).existsByName(Mockito.any());
        Mockito.verify(categoryRepository,Mockito.times(1)).save(Mockito.any(CategoryEntity.class));
        Mockito.verify(serviceHelper,Mockito.times(1)).setCategoryResponseDTO(Mockito.any(CategoryEntity.class),Mockito.anyBoolean(),Mockito.any(),Mockito.any());

    }

    // test for Name or Resources alreadyExists

    @Test
    void testUpdateCategory_ResourceAlreadyExists() {
        Mockito.when(categoryRepository.existsByName(Mockito.any(String.class))).thenReturn(true);

        ResourceAlreadyExistException exception = Assertions.assertThrows(ResourceAlreadyExistException.class, () -> {
            //call the methode which need to be tested
            categoryService.categoryIdPut(1, category);
        });

        Assertions.assertEquals(exception.getMessage(),ErrorCode.RES0001.getMessage());
        Assertions.assertEquals(exception.getErrorCode(),ErrorCode.RES0001.getCode());

        Mockito.verify(categoryRepository,Mockito.times(1)).existsByName(Mockito.any(String.class));
        Mockito.verify(categoryRepository,Mockito.times(0)).save(Mockito.any(CategoryEntity.class));

    }
    // Case 3. categoryIdDelete
    @Test
    void testCategoryIdDelete_Success(){
        Mockito.when(categoryRepository.existsById(1L)).thenReturn(true);
        Mockito.when(serviceHelper.setSuccessResponseDto(Mockito.anyBoolean(),Mockito.any(),Mockito.any())).thenReturn(successResponseDTO);

        SuccessResponseDTO responseDTO = categoryService.categoryIdDelete(1);

        Assertions.assertTrue(responseDTO.getSuccess());

        Mockito.verify(categoryRepository,Mockito.times(1)).existsById(Mockito.any(Long.class));
    }
    // test for NotFound
    @Test
    void testCategoryIdDelete_NotFound(){
        Mockito.when(categoryRepository.existsById(1L)).thenReturn(false);

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            categoryService.categoryIdDelete(1);
        });

        Assertions.assertEquals(exception.getMessage(),ErrorCode.ERR404.getMessage());
        Assertions.assertEquals(exception.getErrorCode(),ErrorCode.ERR404.getCode());

        Mockito.verify(categoryRepository,Mockito.times(1)).existsById(Mockito.any());
        Mockito.verify(categoryRepository,Mockito.times(0)).save(Mockito.any(CategoryEntity.class));
    }

    // Case 4. categoryIdGet
    @Test
    void testCategoryIdGet_Success(){
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(categoryEntity));
        Mockito.when(serviceHelper.setCategoryResponseDTO(Mockito.any(CategoryEntity.class),Mockito.anyBoolean(),Mockito.any(),Mockito.any())).thenReturn(categoryResponseDTO);
        CategoryResponseDTO responseDTO = categoryService.categoryIdGet(1);
        Assertions.assertTrue(responseDTO.getSuccess());
        Assertions.assertNotNull(responseDTO);
        Mockito.verify(categoryRepository,Mockito.times(1)).findById(Mockito.any());
    }

    // test case for Not found
    @Test
    void testCategoryIdGet_NotFound(){
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,()->{
            categoryService.categoryIdGet(1);
        });
        Assertions.assertEquals(exception.getMessage(),ErrorCode.ERR404.getMessage());
        Assertions.assertEquals(exception.getErrorCode(),ErrorCode.ERR404.getCode());
        Mockito.verify(categoryRepository,Mockito.times(1)).findById(Mockito.any());
    }
    // Case 5. getListOfCategory
    @Test
    void testGetListOfCategory_Success(){
        Page<CategoryEntity> productEntityPage = new PageImpl<>(List.of(categoryEntity));
        Mockito.when(categoryRepository.findAll(Mockito.any(Specification.class),Mockito.any(Pageable.class))).thenReturn(productEntityPage);
        Mockito.when(serviceHelper.setListCategoryResponseDTO(Mockito.anyBoolean(),Mockito.anyInt(),Mockito.anyInt(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(categoryListResponseDTO);

        CategoryListResponseDTO responseDTO = categoryService.getListOfCategory(0,1,"name:electric","product:laptop");
        Assertions.assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.getSuccess());

    }
    // test case for Not found
    @Test
    void testGetProducts_NOTFound() {
        // directt create a page object which will contain the pageable data and used in the productRepository.findAll()
        // to get the data with the user entries
        // for now as it was an test case so we are supposing the otp  from the repo will be like productEntity object
        Page<ProductEntity> emptyPage = Page.empty();
        Mockito.when(categoryRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(emptyPage);


        // now call methode on which operation is applying with that invalidValue and then check the response coming from the service methode
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,() ->{
            categoryService.getListOfCategory(0,1,"name:xyz","name:Laptop");
        });
        // now check the response that it matches the expecting data, using Assertions
        Assertions.assertEquals(exception.getMessage(),ErrorCode.ERR404.getMessage());
        Assertions.assertEquals(exception.getMessage(),ErrorCode.ERR404.getMessage());
        // now verify the methods that how much time they called vs expected

    }
}
