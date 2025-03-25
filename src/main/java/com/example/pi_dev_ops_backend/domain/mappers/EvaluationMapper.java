package com.example.pi_dev_ops_backend.domain.mappers;

import com.example.pi_dev_ops_backend.domain.dtos.EvaluationResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.Evaluation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EvaluationMapper
{
    EvaluationMapper INSTANCE = Mappers.getMapper(EvaluationMapper.class);

    @Mapping (source = "evaluation.contractedListing.id", target = "contractedListingId")
    EvaluationResponseDTO toEvaluationResponseDTO(Evaluation evaluation);
}
