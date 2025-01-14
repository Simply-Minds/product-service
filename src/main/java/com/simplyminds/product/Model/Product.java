package com.simplyminds.product.Model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;




@Entity
@Table(name = "product")
public class Product {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "product_unit_id", referencedColumnName = "product_unit_id")
    private ProductUnit productUnit;


    //primary details
    private String name;
    private String SKU;
    @Column(precision = 19, scale = 2)
    private BigDecimal price;
    @Column(name = "quantity_in_stock")
    private Integer quantityInStock = 0;
    private String status;

    //secondary details
    private String description;
    @Column(name = "reorder_level")
    private Integer reorderLevel = 1;
    @Column(name = "image_url")
    private String imageUrl;
    private String brand;
    private double weight;
    private String dimensions;
    private String color;

    private String tags;

    public Product(){}

    public Product(Long productId, Category category, ProductUnit productUnit, String name, String SKU, BigDecimal price, Integer quantityInStock, String status, String description, Integer reorderLevel, String imageUrl, String brand, double weight, String dimensions, String color, String tags) {
        this.productId = productId;

        this.category = new Category();
        this.productUnit = new ProductUnit();
        this.name = name;
        this.SKU = SKU;
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

    public ProductUnit getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(ProductUnit productUnit) {
        this.productUnit = productUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}


//reorder_level integer,
//image_url varchar(500),
//
//quantity_in_stock integer,