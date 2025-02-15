package com.simplyminds.product.repository;

import com.simplyminds.product.entity.CategoryEntity;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> , JpaSpecificationExecutor<CategoryEntity> {

    boolean existsByName(@Size(min = 2, max = 100) String name);
}
