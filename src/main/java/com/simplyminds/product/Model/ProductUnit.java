package com.simplyminds.product.Model;

import jakarta.persistence.*;

import java.util.List;

// class for providing feature to add or chose existing unit of specific product

@Entity
@Table(name = "product_unit")
public class ProductUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_unit_id")
    private Long productUnitId;
    @OneToMany(mappedBy = "productUnit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
    private String unit_name;
    private String unit_spec;
    // unit :- kilogram
    // spec :- kg
    // final output :- kilogram(kg)
    // we can use uppercase lowercase conversion as need

    public ProductUnit(){}
    public ProductUnit(Long productUnitId, String unit_name, String unit_spec,List<Product> products) {
        this.productUnitId = productUnitId;
        this.unit_name = unit_name;
        this.unit_spec = unit_spec;
        this.products = products;
    }

    public Long getProductUnitId() {
        return productUnitId;
    }

    public void setProductUnitId(Long productUnitId) {
        this.productUnitId = productUnitId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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
