package com.simplyminds.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ElementCollection;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private CategoryEntity categoryEntity;

    @ManyToOne
    @JoinColumn(name = "product_unit_id", referencedColumnName = "product_unit_id")
    private ProductUnitEntity productUnitEntity;

    @Column(name = "name")
    private String name;

    @Column(name = "sku")
    private String sku;

    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "quantity_in_stock")
    private Integer quantityInStock = 0;

    private String status;

    //secondary details
    private String description;

    @Column(name = "reorder_level")
    private Integer reorderLevel;

    @Column(name = "image_url")
    private String imageUrl;

    private String brand;
    private double weight;
    private String dimensions;
    private String color;

    @ElementCollection
    private List<String> tags;

    public ProductEntity(){}

    public ProductEntity(Long productId, CategoryEntity categoryEntity, ProductUnitEntity productUnitEntity, String name, String SKU, BigDecimal price, Integer quantityInStock, String status, String description, Integer reorderLevel, String imageUrl, String brand, double weight, String dimensions, String color, List<String> tags) {
        this.productId = productId;
        this.categoryEntity = categoryEntity;
        this.productUnitEntity = productUnitEntity;
        this.name = name;
        this.sku = SKU;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.status = status;
        this.description = description;
        this.reorderLevel = reorderLevel;
        this.imageUrl = imageUrl;
        this.brand = brand;
        this.weight = weight;
        this.dimensions = dimensions;
        this.color = color;
        this.tags = tags;
    }
}