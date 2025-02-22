package com.simplyminds.product.service.impl;


import com.simplyminds.model.Product;
import com.simplyminds.model.ProductListResponseDTO;
import com.simplyminds.model.ProductResponseDTO;
import com.simplyminds.model.SuccessResponseDTO;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.product.enums.ErrorCode;
import com.simplyminds.product.exception.BadRequestException;
import com.simplyminds.product.exception.ResourceAlreadyExistException;
import com.simplyminds.product.mapper.ProductMapper;
import com.simplyminds.product.repository.ProductRepository;
import com.simplyminds.product.service.ProductService;
import com.simplyminds.product.service.ServiceHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends GenericServiceImpl<ProductEntity, ProductRepository> implements ProductService {

    private final ServiceHelper serviceHelper;

    private final ProductMapper productMapper;
    public ProductServiceImpl(@Qualifier("productRepository")  ProductRepository repository, ServiceHelper serviceHelper, ProductMapper productMapper) {
        super(repository);
        this.serviceHelper = serviceHelper;

        this.productMapper = productMapper;
    }

    @Override
    public ProductResponseDTO createProduct(Product productDTO) {
        if (repository.existsBySku(productDTO.getSku())) {
            throw new ResourceAlreadyExistException(ErrorCode.RES0001.getCode(),ErrorCode.RES0001.getMessage());
        }

        ProductEntity productEntity = productMapper.productDTOToProductEntity(productDTO);
        ProductEntity savedProduct = super.createObject(productEntity);
        return serviceHelper.setProductResponseDTO(savedProduct,true,null,null);

    }

    @Override
    public SuccessResponseDTO productsIdDelete(Integer id) {

        return serviceHelper.setSuccessResponseDto(super.DeleteObject(id),null,null);
    }

    @Override
    public ProductResponseDTO productsIdPut(Integer id, Product productDTO) {
        if (id == null) {
            throw new BadRequestException(ErrorCode.BAD0001.getCode(),ErrorCode.BAD0001.getMessage());
        }
        if (repository.existsBySku(productDTO.getSku())) {
            throw new ResourceAlreadyExistException(ErrorCode.RES0001.getCode(),ErrorCode.RES0001.getMessage());
        }

        ProductEntity productEntity = productMapper.productDTOToProductEntity(productDTO);
        ProductEntity savedProduct = super.objectsIdPut(id,productEntity);
        return serviceHelper.setProductResponseDTO(savedProduct,true,null,null);
    }

    @Override
    public ProductResponseDTO productsIdGet(Integer id) {
       return serviceHelper.setProductResponseDTO(super.objectsIdGet(id),true,null,null);
    }

    @Override
    public ProductListResponseDTO getListOfProducts(Integer page, Integer size, String filter, String search) {
        Page<ProductEntity> productEntities = super.getListOfObjects(page,size,filter,search);
        return serviceHelper.setListProductResponseDTO(true,page,size,productEntities,null,null);
    }
}
