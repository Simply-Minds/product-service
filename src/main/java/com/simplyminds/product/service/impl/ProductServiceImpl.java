package com.simplyminds.product.service.impl;

import com.simplyminds.common.service.impl.GenericServiceImpl;
import com.simplyminds.model.*;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.common.enums.ErrorCode;
import com.simplyminds.common.exception.*;
import com.simplyminds.product.mapper.ProductMapper;
import com.simplyminds.product.repository.ProductRepository;
import com.simplyminds.product.service.ProductService;
import com.simplyminds.product.service.ServiceHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl extends GenericServiceImpl<ProductEntity, ProductRepository> implements ProductService {

    private final ServiceHelper serviceHelper;
    private final ProductMapper productMapper;


    public ProductServiceImpl(@Qualifier("productRepository") ProductRepository repository, ServiceHelper serviceHelper, ProductMapper productMapper) {
        super(repository);
        this.serviceHelper = serviceHelper;
        this.productMapper = productMapper;

    }

    @Override
    public ProductResponseDTO createProduct(Product productDTO) {

        if (repository.existsBySku(productDTO.getSku())) {
            throw new ResourceAlreadyExistException(ErrorCode.RES0001.getCode(),ErrorCode.RES0001.getMessage());
        }

        return serviceHelper.setProductResponseDTO(super.createObject(productMapper.productDTOToProductEntity(productDTO)), true, null, null);
    }

    @Override
    public ProductListResponseDTO getListOfProducts(Integer page, Integer size, String filter, String search) {
        return serviceHelper.setListProductResponseDTO(true, page, size, super.getListOfObjects(page, size, filter, search), null, null);
    }

    @Override
    public SuccessResponseDTO productsIdDelete(Integer id) {

        return serviceHelper.setSuccessResponseDto(super.DeleteObject(id),null,null);
    }
    @Override
    public ProductResponseDTO productsIdPut(Integer id, Product productDTO) {
        if (id == null) {
            throw new BadRequestException(ErrorCode.BAD0001.getCode(), ErrorCode.BAD0001.getMessage());
        }        if (repository.existsBySku(productDTO.getSku())) {
            throw new ResourceAlreadyExistException(ErrorCode.RES0001.getCode(),ErrorCode.RES0001.getMessage());
        }
        return serviceHelper.setProductResponseDTO(super.objectsIdPut(id, productMapper.productDTOToProductEntity(productDTO)), true, null, null);
    }

    @Override
    public ProductResponseDTO productsIdGet(Integer id) {
        return serviceHelper.setProductResponseDTO(super.objectsIdGet(id), true, null, null);
    }
}
