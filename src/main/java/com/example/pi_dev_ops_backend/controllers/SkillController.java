package com.example.pi_dev_ops_backend.controllers;

import com.example.pi_dev_ops_backend.domain.dtos.SkillRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.SkillResponseDTO;
import com.example.pi_dev_ops_backend.domain.queryParams.PaginationParams;
import com.example.pi_dev_ops_backend.services.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping ("/skills")
@RequiredArgsConstructor
public class SkillController
{
    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<Page<SkillResponseDTO>> findAll(PaginationParams paginationParams)
    {
        Page<SkillResponseDTO> skills = skillService.findAll(paginationParams);
        return ResponseEntity.ok().body(skills);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<SkillResponseDTO> findById(@PathVariable Long id)
    {
        SkillResponseDTO responseDTO = skillService.findById(id);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping
    public ResponseEntity<SkillResponseDTO> create(@RequestBody SkillRequestDTO skillRequestDTO) throws URISyntaxException
    {
        SkillResponseDTO responseDTO = skillService.create(skillRequestDTO);
        return ResponseEntity.created(new URI("/skills/" + responseDTO.id())).body(responseDTO);
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<SkillResponseDTO> update(@PathVariable Long id, @RequestBody SkillRequestDTO requestDTO)
    {
        SkillResponseDTO responseDTO = skillService.update(id, requestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        skillService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
