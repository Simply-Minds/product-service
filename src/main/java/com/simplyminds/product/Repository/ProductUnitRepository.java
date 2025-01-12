package com.simplyminds.product.Repository;

import com.simplyminds.product.Model.ProductUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUnitRepository extends JpaRepository<ProductUnit,Long> {
}
