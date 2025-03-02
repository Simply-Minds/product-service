package com.simplyminds.product.Specification.impl;

import com.simplyminds.product.Specification.SpecificationHelper;
import com.simplyminds.product.dto.SpecificationResponseDto;
import com.simplyminds.product.enums.SearchParameters;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.logging.Logger;
public class GenericSpecification<T> implements Specification<T> {
    private static final Logger logger = Logger.getLogger(GenericSpecification.class.getName());

    private final SearchCriteria searchCriteria;
    // we will set it here so that no need to send explicitly the source entity .
    private Class<?> targetEntity;

    SpecificationHelper specificationHelper = new SpecificationHelperImpl();


    // Constructor injection instead of @Autowired
    public GenericSpecification(SearchCriteria criteria
                               ) {
        this.searchCriteria = criteria;


    }

   private String fieldName;
    private String field;
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // First check if this is a join field
        if (isJoinField(searchCriteria.getKey())) {

            return handleJoinPredicate(root, builder);
        } else {
            return handleSimplePredicate(root, builder);
        }
    }

    private Predicate handleJoinPredicate(Root<T> root, CriteriaBuilder builder) {
        SpecificationResponseDto response = specificationHelper.getHelp(
                root.getModel().getJavaType(),
                targetEntity,
                root,
                field
        );

        if (!response.getSuccess() || response.getJoin() == null) {
            logger.warning(response.getErrorMessage());
            return builder.conjunction(); // Return an always-true predicate to prevent errors
        }
        Join<?, ?> join = response.getJoin();
        return createPredicateForField(join.get(fieldName), builder);
    }

    private Predicate handleSimplePredicate(Root<T> root, CriteriaBuilder builder) {
        return createPredicateForField(root.get(searchCriteria.getKey()), builder);
    }

    private Predicate createPredicateForField(Path<?> path, CriteriaBuilder builder) {
        if (path.getJavaType() == String.class) {
            if (searchCriteria.getOperation().equals(":")) {
                return builder.like(path.as(String.class), "%" + searchCriteria.getValue() + "%");
            } else {
                return builder.equal(path, searchCriteria.getValue());
            }
        } else {
            return builder.equal(path, searchCriteria.getValue());
        }
    }

    private boolean isJoinField(String key) {
        return isCategory(key) || isUnit(key);
    }

    // we can add as many joins as want
    // we will definitely try to make this  generic too
    private boolean isCategory(String key) {
   if( key.equalsIgnoreCase(SearchParameters.Category.getName()) ||
                key.equalsIgnoreCase(SearchParameters.Category.getSynonyms()) ||
                key.equalsIgnoreCase(SearchParameters.Category.getShortCuts()))
   {
       targetEntity = SearchParameters.Category.getFieldType().getSimpleName().getClass();
       fieldName = SearchParameters.Category.getFieldName();
       field =  SearchParameters.Category.getField();
       return true;
   }
        return false;
    }

    private boolean isUnit(String key) {
      if( key.equalsIgnoreCase(SearchParameters.Unit.getName()) ||
                key.equalsIgnoreCase(SearchParameters.Unit.getSynonyms()) ||
                key.equalsIgnoreCase(SearchParameters.Unit.getShortCuts()))
      {
          targetEntity = SearchParameters.Unit.getFieldType().getSimpleName().getClass();
        fieldName = SearchParameters.Unit.getFieldName();
          field =  SearchParameters.Unit.getField();
          return true;

      }
      return false;

    }

}