package com.simplyminds.product.service.impl;

import com.simplyminds.model.*;
import com.simplyminds.product.Specification.ProductSpecification;
import com.simplyminds.product.Specification.SearchCriteria;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.product.enums.ErrorCode;
import com.simplyminds.model.ProductResponseDTO;
import com.simplyminds.product.exception.BadRequestException;
import com.simplyminds.product.exception.NotFoundException;
import com.simplyminds.product.exception.ResourceAlreadyExistException;
import com.simplyminds.product.mapper.ProductMapper;
import com.simplyminds.product.repository.CategoryRepository;
import com.simplyminds.product.repository.ProductRepository;
import com.simplyminds.product.service.ProductService;
import com.simplyminds.product.service.ServiceHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final ServiceHelper serviceHelper;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapper productMapper, CategoryRepository categoryRepository, ServiceHelper serviceHelper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.serviceHelper = serviceHelper;
    }

    /**
     * Create a new product in the catalog.
     *
     * @param productDTO the product details to be created
     * @return the created product as a DTO
     * @throws BadRequestException if the SKU already exists
     */
    @Override
    public ProductResponseDTO createProduct(Product productDTO) {
        // Validate unique SKU
        if (productRepository.existsBySku(productDTO.getSku())) {
            throw new ResourceAlreadyExistException(ErrorCode.RES0001.getCode(), "SKU already exists.");
        }
        // Map DTO to entity
        ProductEntity product = productMapper.productDTOToProductEntity(productDTO);
        // Save the product
        ProductEntity savedProduct = productRepository.save(product);
        // calling the serviceHelper class methode to produce the response as well as convert the product back into productDto
        return  serviceHelper.setProductResponseDTO(savedProduct, true, null, null);
    }

    /**
     * Retrieves a List of products with pagination and sorting support.
     *
     * @param page        The no of page to retrieve.
     * @param size        The no of elements(objects) per page.
     * @param filter      The filter to apply (find products by category).
     * @param search      the query that will converted into words and then perform tasks for filtering (basic)
     * @return The list of products with filter applied if found; otherwise, returns response with error code, message,data=null.
     * @throws IllegalArgumentException if the @Param page and size, sortBy are invalid .
     * @throws RuntimeException         if error in server or an exceptional error
     * @since 1.0
     */


    @Override
    public ProductListResponseDTO getListOfProducts(Integer page, Integer size, String filter, String search) {

        // combining the filter first to the combined conditions so that the the filter will apply fist.
        // like filer: category:electric, searched query: name:laptop
        // finally this both conditions will fit together acc to order and filter the data accordingly
        String combinedConditions = Stream.of(filter, search)
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(","));


        Pattern pattern = Pattern.compile("(\\w+?)(:|>|<|!=)(\\w+?)([^,]+)");
       Matcher matcher = pattern.matcher(combinedConditions);
       Specification<Product> spec = null;


       while(matcher.find()){
           SearchCriteria criteria = new SearchCriteria(
                   matcher.group(1),
                   matcher.group(2),
                   matcher.group(3)
           );

           ProductSpecification productSpecification = new ProductSpecification(criteria);

           spec = (spec == null) ? Specification.where(productSpecification) : spec.and(productSpecification);
       }

        return getPaginatedData(page,size,spec);

    }

    public ProductListResponseDTO getPaginatedData(Integer page, Integer size,Specification<Product> spec) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = productRepository.findAll(spec,pageable);
        if (productsPage.isEmpty()) {
            throw new NotFoundException(ErrorCode.ERR404.getCode(),"Not found.");
        }
        return serviceHelper.setListProductResponseDTO(true,page,size,productsPage,null,null);

    }



    /**
     * delete a product from the inventory by product id.
     *
     * @param id the ID of product to be deleted
     * @return the SuccessResponseDTO with Success message; returns null if not found
     */

    @Override
    public SuccessResponseDTO productsIdDelete(Integer id) {
        if (id == null||id<=0) {
            throw new BadRequestException(ErrorCode.BAD0001.getCode(),"Invalid id.");
        }
        Optional<ProductEntity> product =  productRepository.findById(id.longValue());


        if (product.isEmpty()) {

            throw new NotFoundException(ErrorCode.ERR404.getCode(),"Product id not found.");

        }

        ProductEntity productEntity = product.get();
                productRepository.delete(productEntity);
              SuccessResponseDTO successResponseDTO = new SuccessResponseDTO();
              successResponseDTO.success(true);
              successResponseDTO.errorCode(null);
              successResponseDTO.errorMessage(null);
              successResponseDTO.data(null);
              return successResponseDTO;

    }

    /**
     * update or edit an existing product from the inventory by product id.
     *
     * @param id the ID of product to be updated
     * @return the ProductResponseDTO with updated details
     */
    @Override
    public ProductResponseDTO productsIdPut(Integer id, Product productDTO) {
        if (id == null || id <=0) {
            throw new BadRequestException(ErrorCode.BAD0001.getCode(),"Invalid id.");
        }
        // Validate unique SKU
        if (productRepository.existsBySku(productDTO.getSku())) {
            throw new ResourceAlreadyExistException(ErrorCode.RES0001.getCode(), "SKU already exists.");
        }
        // Map DTO to entity
        ProductEntity product = productMapper.productDTOToProductEntity(productDTO);
        // Save the product
        ProductEntity savedProduct = productRepository.save(product);

       return serviceHelper.setProductResponseDTO(savedProduct,true,null,null);
    }

    /**
     * get a product by product id.
     *
     * @param id the ID of product to be retrieved
     * @return the ProductResponseDTO with updated details
     */

    @Override
    public ProductResponseDTO productsIdGet(Integer id) {

        if((id<=0)) {
            throw new BadRequestException(ErrorCode.BAD0001.getCode(),"Invalid id.");
        }
                Optional<ProductEntity> product =  productRepository.findById(id.longValue());

            if (product.isEmpty()) {

                throw new NotFoundException(ErrorCode.ERR404.getCode(),"Product not found.");


            }
        ProductEntity productEntity = product.get();

               return serviceHelper.setProductResponseDTO(productEntity,true,null,null);
        }

}

