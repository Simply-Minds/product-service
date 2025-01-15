package com.simplyminds.product.mapper;

import com.simplyminds.model.Product;
import com.simplyminds.product.entity.CategoryEntity;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.product.entity.ProductUnitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Map a Product entity to a ProductDto.
     *
     * @param productEntity the product entity
     * @return the corresponding ProductDto
     */
    @Mapping(source = "categoryEntity.categoryId", target = "categoryId")
    @Mapping(source = "productUnitEntity.productUnitId", target = "productUnitId")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatusToEnum")
    Product productEntityToProductDTO(ProductEntity productEntity);

    /**
     * Map a productDto to a Product entity.
     *
     * @param productDto the product DTO
     * @return the corresponding Product entity
     */
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatus")
    @Mapping(source = "categoryId", target = "categoryEntity", qualifiedByName = "categoryIdToEntity")
    @Mapping(source = "productUnitId", target = "productUnitEntity", qualifiedByName = "productUnitIdToEntity")
    ProductEntity productDTOToProductEntity(Product productDto);

    @Named("mapStatus")
    default String mapStatus(Product.StatusEnum statusEnum) {
        return statusEnum.toString();
    }

    @Named("mapStatusToEnum")
    default Product.StatusEnum mapStatusToEnum(String status) {
        if (status == null) {
            return null;
        }
        return Product.StatusEnum.valueOf(status.toUpperCase()); // Converts the String to the corresponding enum
    }

    @Named("categoryIdToEntity")
    default CategoryEntity categoryIdToEntity(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(categoryId);  // Set the categoryId, no need to fetch from DB
        return categoryEntity;
    }

    @Named("productUnitIdToEntity")
    default ProductUnitEntity productUnitIdToEntity(Long productUnitId) {
        if (productUnitId == null) {
            return null;
        }
        ProductUnitEntity productUnitEntity = new ProductUnitEntity();
        productUnitEntity.setProductUnitId(productUnitId);  // Set the productUnitId
        return productUnitEntity;
    }
}
