package com.simplyminds.product.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "product_unit")
public class ProductUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_unit_id")
    private Long productUnitId;
    private String unit_name;
    private String unit_spec;
    // unit :- kilogram
    // spec :- kg
    // final output :- kilogram(kg)
    // we can use uppercase lowercase conversion as need

    public ProductUnit(){}
    public ProductUnit(Long productUnitId, String unit_name, String unit_spec) {
        this.productUnitId = productUnitId;
        this.unit_name = unit_name;
        this.unit_spec = unit_spec;
    }

    public Long getProductUniIdl() {
        return productUnitId;
    }

    public void setProductUniIdl(Long productUniIdl) {
        this.productUnitId = productUniIdl;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getUnit_spec() {
        return unit_spec;
    }

    public void setUnit_spec(String unit_spec) {
        this.unit_spec = unit_spec;
    }
}
