package com.simplyminds.product.service;

import com.simplyminds.model.*;

public interface CategoryService {

        CategoryResponseDTO createCategory(Category categoryDTO);

        CategoryListResponseDTO getListOfCategory(Integer page, Integer size, String filter, String search);

        SuccessResponseDTO categoryIdDelete(Integer id);

        CategoryResponseDTO categoryIdPut(Integer id, Category categoryDTO);

        CategoryResponseDTO categoryIdGet(Integer id);



}
