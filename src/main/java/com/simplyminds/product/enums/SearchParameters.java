package com.simplyminds.product.enums;

import lombok.Getter;
/**
 * Enum representing various SearchParameters
 * used for Search operations over the application.
 * <p>
 * This enum is structured in categories such as:
 * <ul>
 *     <li>Entity based like : categoryEntity,productUnitEntity</li>
 *     <li>Based on properties or instance variables of the product entity like : name,price,sku</li>
 *     <li>Based on ShortCuts (not for now)</li>
 * </ul>
 * </p>
 * Each SearchParameter can contain : Name, Antonym or synonyms , ShortCuts.
 * and an associated Meaning of that field - for Ai based search
 */


@Getter
public enum SearchParameters {

        /**
         * Category for the operations on categories .
         */
        Category("Category","type", "ctg","categoryEntity","name"),
        Unit("Unit","kind", "un","productUnitEntity","unitSpec");

        // TODO we can add here as much fields as we want. like meanings of fields for ai based search operations.

             private String name;
             private String synonyms;
             private String ShortCuts;
             private String field;
             private String fieldName;
        /**
         * Constructs an instance of the SearchParameters enum with the specified values.
         * @param name the unique error code.
         * @param synonyms the synonyms that can be used as an alternative for the field.
         * @param shortCuts the shortCuts for a particular field based on user preferences
         */
    SearchParameters(String name,String synonyms, String shortCuts, String field,String fieldName) {
        this.name = name;
        this.synonyms = synonyms;
        this.ShortCuts = shortCuts;
        this.field = field;
        this.fieldName = fieldName;
    }
}

