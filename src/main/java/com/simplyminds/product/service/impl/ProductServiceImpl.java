package com.simplyminds.product.service.impl;

import com.simplyminds.model.*;
import com.simplyminds.product.entity.CategoryEntity;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
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
        // Map entity back to DTO
        Product entityToProductDTO = productMapper.productEntityToProductDTO(savedProduct);
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setData(entityToProductDTO);
        productResponseDTO.setSuccess(Boolean.TRUE);
        return productResponseDTO;
    }

    /**
     * Retrieves a List of products with pagination and sorting support.
     *
     * @param page     The no of page to retrieve.
     * @param size     The no of elements(objects) per page.
     * @param filter The filter to apply (find products by category).
     * @param filterValue    the value of filter to use like: category->food->chips or lowStock->no value needed
     * @param search the query that will converted into words and then perform tasks for filtering (basic)
     * @return The list of products with filter applied if found; otherwise, returns response with error code, message,data=null.
     * @throws IllegalArgumentException if the @Param page and size, sortBy are invalid .
     * @throws RuntimeException         if error in server or an exceptional error
     * @since 1.0
     */

    // methode that will call another methode to do work
    @Override
    public ProductListResponseDTO getListOfProducts(Integer page, Integer size, String filter, String filterValue, String search) {
        // if filter is not provided it means return default products without filtering (ASSUMING).
        if (filter==null&&filterValue==null&&search==null){
            String sortBy = "price";// default sorting
            boolean ascending = true;
            ProductListResponseDTO productListResponseDTO = getDefaultProductList(page,size,sortBy,ascending);
            return productListResponseDTO;
        }

        if(search != null){
            // call the search engine class and get the fields to be filtered
            // and then call the getListOfProductsByFilter methode with that filter and filter Value methode
           String [] data = getTheFilteredDataBySearch(search);
           filter = data[0];
           filterValue = data[1];

            List<ProductEntity> productEntities  = getListOfProductsByFilter(filter,filterValue);
            if (productEntities==null||productEntities.isEmpty()){
                throw new NotFoundException(ErrorCode.ERR404.getCode(),"Not found.");
            }
            return getPaginatedProducts(productEntities, page, size);
        }

        List<ProductEntity> productEntities  = getListOfProductsByFilter(filter,filterValue);
        if (productEntities == null || productEntities.isEmpty()) {
            throw new NotFoundException(ErrorCode.ERR404.getCode(), "Not found.");
        }
            return getPaginatedProducts(productEntities, page, size);

    }


    @Override
    public ProductListResponseDTO getDefaultProductList(Integer page, Integer size, String sortBy, boolean ascending) {

        // sorting by using sort class with two parameters sortBy,ascending
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);


        // page with all data
        Page<ProductEntity> productsPage = productRepository.findAll((Pageable) pageable);
        if (productsPage.isEmpty()) {

            throw new NotFoundException(ErrorCode.ERR404.getCode(),"Products not found.");

        }
        // only data (products)
        List<Product> products = new ArrayList<>();
        // Map entities back to DTOs
        for (ProductEntity productEntity : productsPage){
            // Map entity back to DTO
            Product entityToProductDTO = productMapper.productEntityToProductDTO(productEntity);
            products.add(entityToProductDTO);
        }

        ProductListResponseDTOPagination pagination = new ProductListResponseDTOPagination();
        pagination.setTotalObjects(((int) productsPage.getTotalElements()));
        pagination.currentPage(page);
        pagination.setTotalPages(productsPage.getTotalPages());
        pagination.currentSize(size);
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        productListResponseDTO.setSuccess(Boolean.TRUE);
        productListResponseDTO.data(products);
        productListResponseDTO.errorCode(null);
        productListResponseDTO.errorMessage(null);
        productListResponseDTO.setPagination(pagination);
        productListResponseDTO.getPagination().getTotalObjects();
        productListResponseDTO.getPagination().getCurrentSize();
        productListResponseDTO.getPagination().getCurrentPage();
        productListResponseDTO.getPagination().getTotalPages();
        return productListResponseDTO;

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
        // Map entity back to DTO
        Product entityToProductDTO = productMapper.productEntityToProductDTO(savedProduct);
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setData(entityToProductDTO);
        productResponseDTO.setSuccess(Boolean.TRUE);
        return productResponseDTO;
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
                Product entityToProductDTO = productMapper.productEntityToProductDTO(productEntity);
                ProductResponseDTO productResponseDTO = new ProductResponseDTO();
                productResponseDTO.data(entityToProductDTO);
                productResponseDTO.errorCode(null);
                productResponseDTO.errorMessage(null);
                productResponseDTO.setSuccess(true);
                return productResponseDTO;
        }





    // logic to retrieve all the instance variables from class to compare
    ProductEntity products = new ProductEntity();
    Field[] fields = products.getClass().getDeclaredFields();
    // filtering methode
    /**
     * methode used in getListOfProducts to provide filtered products
     *
     * @param filter the filter to be applied like:byCategory,byLowStock
     * @return the List<ProductEntity> which contains the filtered products
     */
    public List<ProductEntity>  getListOfProductsByFilter(String filter , String filterValue) {

        List<ProductEntity> filteredProducts = new ArrayList<>();
        // filter by category
        ProductEntity productE = new ProductEntity();
        Field[] fields = productE.getClass().getDeclaredFields();
        String match = null;
        for (Field field : fields){
            if (field.getName().equalsIgnoreCase(filter)){
                match = field.getName();
            }
        }

        if(checkSpell("category",filter)){
            for (CategoryEntity categoryEntity : categoryRepository.findAll()) {

                if (checkSpell(filterValue,categoryEntity.getName())) {

                    for (ProductEntity productEntity : productRepository.findAll()) {
                        if (Objects.equals(productEntity.getCategoryEntity().getCategoryId(), categoryEntity.getCategoryId())){
                            filteredProducts.add(productEntity);

                        }

                    }

                }

            }

        }
        // Filter for low stock assuming the low stock will be defined by the reorder level
        else  if(checkSpell("lowstock",filter)){

            for (ProductEntity productEntity : productRepository.findAll()) {

                if (productEntity.getQuantityInStock() >= productEntity.getReorderLevel()) {

                    filteredProducts.add(productEntity);


                }

            }
        }
        // filter by supplier and supplierName
        else if (checkSpell("supplier",filter)) {
            //TODO this will done when supplier service will done
            filteredProducts.add(null);
        }
        // filter by status
        else if (checkSpell("status",filter)){

            for (ProductEntity productEntity : productRepository.findAll()) {

                if (checkSpell(filterValue,productEntity.getStatus())) {

                    filteredProducts.add(productEntity);


                }

            }
        }
        // filter by quantity
        else  if (checkSpell("quantity",filter)||checkSpell("QuantityInStock",filter)){


            for (ProductEntity productEntity : productRepository.findAll()) {

                if (productEntity.getQuantityInStock() == Math.toIntExact(Long.parseLong(filterValue))) {

                    filteredProducts.add(productEntity);

                }

            }
        }
        // filter by name
        else if (checkSpell("name",filter)){

            for (ProductEntity productEntity : productRepository.findAll()) {

                if (checkSpell(filterValue,productEntity.getName())) {

                    filteredProducts.add(productEntity);


                }

            }
        }
        // filter by SKU
        else  if (checkSpell("sku",filter)){

            for (ProductEntity productEntity : productRepository.findAll()) {

                if (checkSpell(filterValue,productEntity.getSku())) {

                    filteredProducts.add(productEntity);


                }

            }
        }
        // idea we can make the substring of the desired filter value and here we are added the functionality of
        // getting response even if the spelling is not correct

        // filter by brand
        else if (checkSpell(filter,"brand")){
            for (ProductEntity productEntity : productRepository.findAll()) {

                if (checkSpell(filterValue,productEntity.getBrand())) {

                    filteredProducts.add(productEntity);


                }

            }
        }


        if (filteredProducts.isEmpty()) {

            return null;

        }

        return filteredProducts;

    }




    // pagination logic
    /**
     * methode used in getListOfProducts to provide pagination support.
     *
     * @param filteredProducts the list of products on which pagination will apply.
     * @return the ProductListResponseDTO which contains all the details of pagination and data.
     */
    public ProductListResponseDTO getPaginatedProducts(List<ProductEntity> filteredProducts, int page, int size) {


        // PAGINATION logic
        // calculating essential variables
        Integer totalElements = filteredProducts.size();
        Integer totalPages = (int) Math.ceil((double) totalElements / size);
        Integer startIndex = page * size;
        Integer endIndex = Math.min(startIndex + size, totalElements);

        // return empty list if index is out of bound
        if (startIndex >= totalElements) {
            return new ProductListResponseDTO();
        }
        // creating sublist and then mapping the selected products to product dto
        List<ProductEntity> paginatedProducts = filteredProducts.subList(startIndex, endIndex);
        // now map all products to productDto list
        List<Product> products = new ArrayList<>();
        for (ProductEntity productEntity : paginatedProducts) {
            Product entityToProductDTO = productMapper.productEntityToProductDTO(productEntity);
            products.add(entityToProductDTO);

        }
        ProductListResponseDTOPagination pagination = new ProductListResponseDTOPagination();
        pagination.setTotalObjects(totalElements);
        pagination.currentPage(page);
        pagination.setTotalPages(totalPages);
        pagination.currentSize(size);
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        productListResponseDTO.setSuccess(Boolean.TRUE);
        productListResponseDTO.data(products);
        productListResponseDTO.errorCode(null);
        productListResponseDTO.errorMessage(null);
       productListResponseDTO.setPagination(pagination);
       productListResponseDTO.getPagination().getTotalObjects();
       productListResponseDTO.getPagination().getCurrentSize();
       productListResponseDTO.getPagination().getCurrentPage();
       productListResponseDTO.getPagination().getTotalPages();

        return productListResponseDTO;

    }



    /**
     * Lets break down the Query in the parts of spaces
     * @Param search the Query to be searched.
     * @Return returns the list of products based on the engine work
     * */
    // Locale.ROOT :-
    // going to use regex for he next part of spliting and  reasesrching
    // X,//s,\\S,\w,\W,\p{space}
    // Now lets make filters for the refrenicing variable and for value and
    // then passing them in the getProductsList methode calling with the desired parameter filter and filterValue
    /**
     * we will use  predefined methode to gather the names of instance variable fields
     * So that we can compare them with the query words or parts and if matches then we will store them in
     * A new array as recognized fields so that we can pass them into the getProductsList methode to filter them and we*/
    // Let takes the input in smaller case and then let them used
    //*** alternative ***\\ we can give the response from  here direct
    // FIRST TRYING TO CALL THE EXISTING METHODE


    public String[] getTheFilteredDataBySearch(String search){
        System.out.println("search : "+search);
        String filter = null;
        String filterValue = null;
        String[] words = search.toLowerCase().split("\\s+"); // Set of things that will help to separate values

        int queryLength = words.length;
        // Fetching all the instance variables
        ProductEntity productEntity = new ProductEntity();
        Field[] fields = productEntity.getClass().getDeclaredFields(); //Field[] array contains all the fields in productEntity
        // Now we can use the fields.getName() methode to get the names of each instance variables
        // *** ONE thing we need to convert the name in lowercase each time using the name *** \\
        int fieldLength = fields.length;
        // NEW ARRAY TO STORE THE RECOGNIZED WORDS FOR FURTHER OPERATION
        ArrayList<String> recognizedWordsOfFilter = new ArrayList<>();
        ArrayList<String> recognizedWordsOfFilterValue = new ArrayList<>();


        // **** THIS LOGIC IS FOR PARAMETER 'filter'
        for (int i=0;i<words.length;i++){
            for (Field field : fields){
                // inner loop to treverse over the instance variables (fields)
                String fieldName = field.getName().toLowerCase();
                // we can cut the last six (6) chars  if they equals to "entity"
                // and can also cut the first 6 chars if they = "product"


        if (checkSpell(words[i],fieldName)){
            // adding the recognized word to the recognizedWordsOfFilter arrayList for the 'filter parameter'
            recognizedWordsOfFilter.add(words[i]);
        }


    }
        }
        // **** THIS LOGIC IS FOR PARAMETER 'filterValue'
        boolean isField = false;
        for(String rec:recognizedWordsOfFilter){
            for (int i=0;i<words.length;i++){
                if (!Objects.equals(words[i], rec)){
                    for (Field field : fields){
                        if (checkSpell(words[i],field.getName())){
                           isField = true;
                        }
                    }
                    if (!isField){
                        recognizedWordsOfFilterValue.add(words[i]);
                    }

                }
            }
        }


        //***** NOW i will check each filtervalue with the refrenincing field filter ******\\
        // so that my engine will works as expected dont casr about performance or speed
        // because the first car engine ia also very slow .... good night ceo of jlss mr. jeet solanki - 24-01-2025tm01:27:25

        //++++++ AND REMEMBER TO COMPLETE IT IN THE MORNING ++++++\\
        /**
         * NOTE : THE WORDS ARE CONVERTED IN LOWERCASE BEFORE DOING ANY ACTION DON'T WORRY DARLING .
         * exa of useCase :- price 2000 -> so filter = price, filter value = 2000
         * exa :- category electronic -> so filter = category , filter value = electronics
         * exa :- quantityInStock 20 -> so filter = quantityInStock , filter value = 20
         * exa :- lowStock null -> so filter = lowStock , filter value = null -> means using the reorderLevel to recognize the lowStocks
         * exa :- supplier jeet -> so filter = supplier , filter value = jeet (the name of supplier)
         * NOTE : all these cases are present in the ProductService class so don't worry sweetHeart
         */

        // NOW before calling the getProductsList methode set the filterValue to the first no from the numbers array




        if (!recognizedWordsOfFilterValue.isEmpty()) {
            filterValue = recognizedWordsOfFilterValue.get(0);

        }

        if (!recognizedWordsOfFilter.isEmpty()) {
            filter = recognizedWordsOfFilter.get(0);

        }

        // now finally calling the methode to get the products and then returning them
        return new String[]{filter,filterValue};

    }

    // methode for wrong spelling to allow it using
    public boolean checkSpell(String spell , String expectedSpell){

        if (spell == null || expectedSpell == null ) {
            return false;
        }
        if (spell.equalsIgnoreCase(expectedSpell)){
            return true;
        }


        if (expectedSpell.toLowerCase().endsWith("entity")) {
            expectedSpell = expectedSpell.substring(0,expectedSpell.length()-6);
            System.out.println(expectedSpell);
        }
        int count = 0;
        int maxLength = Math.max(spell.length(), expectedSpell.length());
        int matchPercentage = (maxLength*30)/100;

        for (int i = 0; i < Math.min(spell.length(), expectedSpell.length()); i++) {
            if (spell.charAt(i) == expectedSpell.charAt(i)) {
                count++;
            }
        }
        // check if length is around 60% matches
        return count >= matchPercentage;

    }

}

