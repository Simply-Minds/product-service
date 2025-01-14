package com.simplyminds.product.repository;

import com.simplyminds.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {

    /**
     * Check if a product with the given SKU exists in the database.
     *
     * @param sku the SKU to check
     * @return true if a product with the given SKU exists, false otherwise
     */
    boolean existsBySku(String sku);
}
