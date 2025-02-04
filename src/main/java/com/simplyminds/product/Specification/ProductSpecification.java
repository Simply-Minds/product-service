package com.simplyminds.product.Specification;

import com.simplyminds.model.Product;
import com.simplyminds.product.entity.CategoryEntity;
import com.simplyminds.product.entity.ProductEntity;
import com.simplyminds.product.entity.ProductUnitEntity;
import com.simplyminds.product.enums.SearchParameters;
import com.simplyminds.product.repository.CategoryRepository;
import com.simplyminds.product.repository.ProductUnitRepository;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.logging.Logger;


public class ProductSpecification implements Specification<Product> {
    private static final Logger logger = Logger.getLogger(ProductSpecification.class.getName());

    private SearchCriteria searchCriteria;



    public ProductSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;

    }




    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        /**
         *Using field and field name to avoid the string literals while making the joins
         * field : means the entity or table that we want. like categoryEntity
         * fieldName : means the instance variable or object in that field or entity. like : unitSpec:kg or unitName:kilogram etc
         **/
        String field = searchCriteria.getKey();
        String fieldName;

        /**
         *  To use the instant class instant variables.
         *  we need to create a relation btw them first .
         *  by joining the product entity with categoryEntity
         *  by criteria join api .
         *  WE CAN ALSO GO WITH THE REPOSITORY LAYERS BUT
         *  We cant use another query inside one builder query
         *  Not sure but,i tried it gives issues on some complex cases or even smaller one
         */
        // declaring them first and not initialising
        // because we did not have this need in all the cases.
        Join<ProductEntity,CategoryEntity> categoryJoin = null;
        Join<ProductEntity,ProductUnitEntity> unitJoin = null;

        if (isCategory(field)) {
            // now initialising the previously declared join
            // because we need the category name .
            // as our productEntity only has category id so far
            // so by doing this we are creating an SQL JOIN btw the entity at run time .
            // here root.join() represents the root entity which is product in our case.
            // ** So here may a question will arise.
            // if the root entity is product then why we are using the productEntity in hte join as previously declared?
            // so the ans may be that for using the join() here , we require a relation btw the entity that gonna be joind in the entity
            // so we have that relation in our productEntity no have in product class thats why i think we are using it.
           categoryJoin = root.join(SearchParameters.Unit.getField(),JoinType.INNER);
           field = SearchParameters.Category.getField();// Using the enums to avoid the String literals and for scalebality that in case future we want toi add something new so we dont need to worry about to chage that in all places
            fieldName = SearchParameters.Category.getFieldName();
        } else if (isUnit(field)) {
           unitJoin = root.join(SearchParameters.Unit.getField(),JoinType.INNER);// it means we are getting the string CategoryEntity from the enum.
            field = SearchParameters.Unit.getField();
            fieldName = SearchParameters.Unit.getFieldName();
        } else {
            fieldName = null; // No mapping needed for direct fields
        }



        if (searchCriteria.getOperation().equalsIgnoreCase(":")) {
            if (fieldName != null) {
                // Handle joined entities
                if (categoryJoin != null) {
                    return builder.like(categoryJoin.get(fieldName), "%" + searchCriteria.getValue() + "%");
                } else if (unitJoin != null) {
                    return builder.like(unitJoin.get(fieldName), "%" + searchCriteria.getValue() + "%");
                }
            }

            // Handle normal fields in ProductEntity
            if (root.get(searchCriteria.getKey()).getJavaType() == String.class) {
                return builder.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
            } else {
                return builder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            }
        }

        return null; // No matching operation
    }
    /**
     * Helper methods to avoid bulky code in main logic
     * they just check the user input to the available enum entries
     * */

    private boolean isCategory(String key) {
        return key.equalsIgnoreCase(SearchParameters.Category.getName()) ||
                key.equalsIgnoreCase(SearchParameters.Category.getSynonyms()) ||
                key.equalsIgnoreCase(SearchParameters.Category.getShortCuts());
    }

    private boolean isUnit(String key) {
        return key.equalsIgnoreCase(SearchParameters.Unit.getName()) ||
                key.equalsIgnoreCase(SearchParameters.Unit.getSynonyms()) ||
                key.equalsIgnoreCase(SearchParameters.Unit.getShortCuts());
    }
}