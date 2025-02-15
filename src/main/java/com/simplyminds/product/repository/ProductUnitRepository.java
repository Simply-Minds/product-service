package com.simplyminds.product.repository;

import com.simplyminds.product.entity.ProductUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUnitRepository extends JpaRepository<ProductUnitEntity,Long> {

}
