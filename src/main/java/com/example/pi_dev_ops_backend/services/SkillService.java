package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.SkillRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.SkillResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.Skill;
import com.example.pi_dev_ops_backend.domain.mappers.SkillMapper;
import com.example.pi_dev_ops_backend.domain.queryParams.PaginationParams;
import com.example.pi_dev_ops_backend.repository.SkillRepository;
import com.example.pi_dev_ops_backend.services.exceptions.InvalidArgsException;
import com.example.pi_dev_ops_backend.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkillService
{
    private final SkillRepository skillRepository;

    public Page<SkillResponseDTO> findAll(PaginationParams paginationParams)
    {
        Pageable pageable = PageRequest.of(paginationParams.getPage(), paginationParams.getSize());
        return skillRepository.findAll(pageable).map(SkillMapper.INSTANCE::toSkillResponseDTO);
    }

    public SkillResponseDTO findById(Long id)
    {
        Skill skill = findEntityById(id);
        return SkillMapper.INSTANCE.toSkillResponseDTO(skill);
    }

    public SkillResponseDTO create(SkillRequestDTO skillRequestDTO)
    {
        if (skillRepository.findByName(skillRequestDTO.name()).isPresent())
            throw new InvalidArgsException("Skill already exists");

        Skill skill = new Skill(skillRequestDTO.name());

        return SkillMapper.INSTANCE.toSkillResponseDTO(skillRepository.save(skill));
    }

    public SkillResponseDTO update(Long id, SkillRequestDTO skillRequestDTO)
    {
        Skill skill = findEntityById(id);
        Optional<Skill> skillOptional = skillRepository.findByName(skillRequestDTO.name());
        if (skillOptional.isPresent() && !skill.equals(skillOptional.get()))
            throw new InvalidArgsException("Skill name already exists");

        skill.setName(skillRequestDTO.name());

        return SkillMapper.INSTANCE.toSkillResponseDTO(skillRepository.save(skill));
    }

    public void delete(Long id)
    {
        Skill skill = findEntityById(id);
        skillRepository.delete(skill);
    }

    public Skill findEntityByName(String name)
    {
        return skillRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(Skill.class, "name: " + name));
    }

    Skill findEntityById(Long id)
    {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Skill.class, id));
    }

}
