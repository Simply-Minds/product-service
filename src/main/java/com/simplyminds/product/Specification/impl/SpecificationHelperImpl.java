package com.simplyminds.product.Specification.impl;

import com.simplyminds.product.Specification.SpecificationHelper;
import com.simplyminds.product.dto.SpecificationResponseDto;
import com.simplyminds.product.enums.SearchParameters;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

@Service
public class SpecificationHelperImpl implements SpecificationHelper {



    @Override
    public <T,S,R> SpecificationResponseDto getHelp( Class<T> sourceEntity, Class<S> targetEntity, Root<R> root,String field) {


        // declaring them first and not initialising
        // because we did not have this need in all the cases.
        Join<T, S> join = null;
        Boolean isJoin = true;


        join = root.join(field, JoinType.INNER);


        SpecificationResponseDto specificationResponseDto = new SpecificationResponseDto();
        specificationResponseDto.setErrorMessage(null);
        specificationResponseDto.setErrorCode(null);
        specificationResponseDto.setSuccess(true);
        specificationResponseDto.setIsJoinPresent(isJoin);
        specificationResponseDto.setJoin(join);
        return specificationResponseDto;
    }
}
