package com.simplyminds.product.mapper;

import com.simplyminds.model.Product;
import com.simplyminds.product.entity.ProductEntity;
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
    Product productEntityToProductDTO(ProductEntity productEntity);

    /**
     * Map a productDto to a Product entity.
     *
     * @param productDto the product DTO
     * @return the corresponding Product entity
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "sku", target = "sku")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatus")
    ProductEntity productDTOToProductEntity(Product productDto);

    @Named("mapStatus")
    default String mapStatus(Product.StatusEnum statusEnum) {
        return statusEnum.toString();
    }
}
