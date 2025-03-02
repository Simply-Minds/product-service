package com.simplyminds.product.Specification;

import com.simplyminds.product.dto.SpecificationResponseDto;
import jakarta.persistence.criteria.Root;

public interface SpecificationHelper {
    /**
     * We need a more precise and optimisation in our specification class.
     * that we write less code and use it in more places.
     * so let's create a SpecificationHelper that will be.
     * Responsible for providing the data that on which we want queries.
     **/
    // So first we need a response creater which will help to return all this data
    // we take some parameters
    // table: for creating the join btw entity
    // field:  for key of that entity instanceVariable
    // fieldName: for the
    <T, S, R> SpecificationResponseDto getHelp(Class<T> sourceEntity,
                                               Class<S> targetEntity,
                                               Root<R> root,
                                               String field);
}