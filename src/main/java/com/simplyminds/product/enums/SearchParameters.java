package com.simplyminds.product.enums;

import com.simplyminds.product.entity.CategoryEntity;
import com.simplyminds.product.entity.ProductUnitEntity;
import lombok.Getter;

/**
 * Enum representing various SearchParameters
 * used for Search operations over the application.
 * <p>
 * This enum is structured in categories such as:
 * <ul>
 *     <li>Entity-based like: categoryEntity, productUnitEntity</li>
 *     <li>Based on properties or instance variables of the product entity like: name, price, sku</li>
 *     <li>Based on ShortCuts (not for now)</li>
 * </ul>
 * </p>
 * Each SearchParameter can contain: Name, Synonyms, ShortCuts,
 * and an associated Meaning of that field - for AI-based search.
 */

@Getter
public enum SearchParameters {

    /**
     * Category for the operations on categories.
     */
    Category("Category", "type", "ctg", "categoryEntity", "name", CategoryEntity.class),
    Unit("Unit", "kind", "un", "productUnitEntity", "unitSpec", ProductUnitEntity.class);

    // Enum fields
    private final String name;
    private final String synonyms;
    private final String shortCuts;
    private final String field;
    private final String fieldName;
    private final Class<?> fieldType;

    /**
     * Constructs an instance of the SearchParameters enum with the specified values.
     *
     * @param name      The unique name of the parameter.
     * @param synonyms  Alternative names or synonyms for the field.
     * @param shortCuts Shortcut keys for quick access.
     * @param field     The actual database field associated.
     * @param fieldName The user-friendly name of the field.
     * @param fieldType The Java class representing the entity.
     */
    SearchParameters(String name, String synonyms, String shortCuts, String field, String fieldName, Class<?> fieldType) {
        this.name = name;
        this.synonyms = synonyms;
        this.shortCuts = shortCuts;
        this.field = field;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }
}
