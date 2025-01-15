package com.simplyminds.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_unit")
@Getter
@Setter
public class ProductUnitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_unit_id")
    private Long productUnitId;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "unit_spec")
    private String unitSpec;

    public ProductUnitEntity(){}
    public ProductUnitEntity(Long productUnitId, String unitName, String unitSpec) {
        this.productUnitId = productUnitId;
        this.unitName = unitName;
        this.unitSpec = unitSpec;
    }
}
