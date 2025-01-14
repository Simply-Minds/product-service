package com.simplyminds.product.Model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


public class ProductDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;
    private String name;
    @Column(precision = 19, scale = 2)
    private BigDecimal price;
    @Column(name = "quantity_in_stock")
    private Integer quantityInStock = 0;

    public ProductDTO(Long productId, Category category, String name, BigDecimal price, Integer quantityInStock) {
        this.productId = productId;
        this.category = category;
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}
