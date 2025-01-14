package com.simplyminds.product.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "category")
@Data
@Builder
public class CategoryEntity {

    @Id
    @GeneratedValue(generator = "UUID") // Use the UUID generator
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator") // Define the UUID generator strategy
    @Column(name = "category_id", columnDefinition = "VARCHAR(36)")
    private UUID categoryId;
    private String name;
    private String description;

    public CategoryEntity(){}

    public CategoryEntity(UUID categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
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
