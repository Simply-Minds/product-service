package com.simplyminds.product.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

// class for providing feature to organize products or inventory in small chunks(category)

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(generator = "UUID") // Use the UUID generator
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator") // Define the UUID generator strategy
    @Column(name = "category_id", columnDefinition = "varbinary(36)")
    private String categoryId;

    private String name;
    private String description;

    public Category(){}

    public Category(String categoryId, String name, String description,List<Product> products) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;

    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String  categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
