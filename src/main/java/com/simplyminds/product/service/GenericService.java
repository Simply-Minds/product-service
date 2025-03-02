package com.simplyminds.product.service;

import com.simplyminds.model.Product;
import com.simplyminds.model.ProductListResponseDTO;
import com.simplyminds.model.ProductResponseDTO;
import com.simplyminds.model.SuccessResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GenericService<T> {
   boolean DeleteObject(Integer id);
   T createObject(T object);
   T objectsIdPut(Integer id, T object);
   T objectsIdGet(Integer id);
   Page<T> getListOfObjects(Integer page, Integer size, String filter, String search);



}
