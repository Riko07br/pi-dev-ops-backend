package com.example.pi_dev_ops_backend.domain.mappers;

import com.example.pi_dev_ops_backend.domain.dtos.SkillResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SkillMapper
{
    SkillMapper INSTANCE = Mappers.getMapper(SkillMapper.class);

    SkillResponseDTO toSkillResponseDTO(Skill skill);
}
