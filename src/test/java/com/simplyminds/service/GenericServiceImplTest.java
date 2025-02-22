package com.simplyminds.service;

import com.simplyminds.product.entity.CategoryEntity;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.product.service.impl.GenericServiceImpl;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class) // Enables Mockito's annotation processing
public class GenericServiceImplTest<T> {

    @Mock
    private TestRepository<T> repository;  // Mocking repository with both interfaces

    @InjectMocks
    private GenericServiceImpl<T, TestRepository<T>> genericService;

    @BeforeEach
    void setUp() {
        genericService = new GenericServiceImpl<>(repository); // Manually inject the mock
    }

    @ParameterizedTest
    @MethodSource("inputObject")
    void testCreateObject_Success(T object) {
        Mockito.when(repository.save(Mockito.any())).thenReturn(object);
        T data = genericService.createObject(object);

        Assertions.assertNotNull(data);
        Mockito.verify(repository,Mockito.times(1)).save(Mockito.any());

    }
    @ParameterizedTest
    @MethodSource("inputObject_id")
    void testObjectsUpdate_Success(int id,T object) {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(object));
        Mockito.when(repository.save(Mockito.any())).thenReturn(object);
        T data = genericService.objectsIdPut(id,object);

        Assertions.assertNotNull(data);
        Mockito.verify(repository,Mockito.times(1)).save(Mockito.any());
    }
    // case 3. deleteObject
    @ParameterizedTest
    @MethodSource("input_id")
    void testObjectDelete_Success(int id){
        Mockito.when(repository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(repository).deleteById(1L);
        boolean data = genericService.DeleteObject(id);
        Assertions.assertTrue(data);
        Mockito.verify(repository,Mockito.times(1)).deleteById(Mockito.any());
    }

    // case 4. getLisOfObjects
    @ParameterizedTest
    @MethodSource("inputObject_List")
    void testGetListOfObjects_Success(Page<T> objectsPage){

        Mockito.when(repository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(objectsPage);

        Page<T> data = genericService.getListOfObjects(0,1,"id:1",null);

        Assertions.assertNotNull(data);
        Assertions.assertEquals(data.getTotalElements(),1);
        Mockito.verify(repository,Mockito.times(1)).findAll(Mockito.any(Specification.class),Mockito.any(Pageable.class));

    }
    // Case 5. getObjectById
    @ParameterizedTest
    @MethodSource("inputObject_id")
    void testGetObjectById_Success(int id,T object){
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(object));
        T data = genericService.objectsIdGet(id);
        Assertions.assertNotNull(data);
        Assertions.assertEquals(data,object);
        Mockito.verify(repository,Mockito.times(1)).findById(Mockito.any());
    }



    /**
     * Method for the tests data input creation
     */
    static Stream<Arguments> inputObject() {
        ProductEntity product = new ProductEntity();
        product.setProductId(1L);
        product.setName("laptop");

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(1L);
        categoryEntity.setName("electric");

        return Stream.of(
                Arguments.arguments(product),
                Arguments.arguments(categoryEntity)
        );
    }

    /**
     * Method for the tests data input creation
     */
    static Stream<Arguments> inputObject_List() {
        ProductEntity product = new ProductEntity();
        product.setProductId(1L);
        product.setName("laptop");
        Page<ProductEntity> productPage = new PageImpl<>(List.of(product));
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(1L);
        categoryEntity.setName("electric");
        Page<CategoryEntity> categoryEntityPage = new PageImpl<>(List.of(categoryEntity));

        return Stream.of(
                Arguments.arguments(productPage),
                Arguments.arguments(categoryEntityPage)
        );
    }
    static Stream<Arguments> inputObject_id() {
        ProductEntity product = new ProductEntity();
        product.setProductId(1L);
        product.setName("laptop");

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(1L);
        categoryEntity.setName("electric");

        return Stream.of(
                Arguments.arguments(product.getProductId().intValue(),product),
                Arguments.arguments(categoryEntity.getCategoryId().intValue(),categoryEntity)
        );
    }

    static Stream<Arguments> input_id() {
        ProductEntity product = new ProductEntity();
        product.setProductId(1L);
        product.setName("laptop");

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(1L);
        categoryEntity.setName("electric");

        return Stream.of(
                Arguments.arguments(product.getProductId().intValue()),
                Arguments.arguments(categoryEntity.getCategoryId().intValue())
        );
    }

    /**
     * Helper interface to combine JpaRepository & JpaSpecificationExecutor
     */
    interface TestRepository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {}
}
