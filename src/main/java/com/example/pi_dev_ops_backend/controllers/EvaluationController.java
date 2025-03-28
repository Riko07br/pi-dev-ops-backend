package com.example.pi_dev_ops_backend.controllers;

import com.example.pi_dev_ops_backend.domain.dtos.EvaluationRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.EvaluationResponseDTO;
import com.example.pi_dev_ops_backend.domain.queryParams.EvaluationPaginationParams;
import com.example.pi_dev_ops_backend.services.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping ("/evaluations")
@RequiredArgsConstructor
public class EvaluationController
{
    private final EvaluationService evaluationService;

    @GetMapping
    public ResponseEntity<Page<EvaluationResponseDTO>> findAll(EvaluationPaginationParams paginationParams)
    {
        Page<EvaluationResponseDTO> evaluations = evaluationService.findAll(paginationParams);
        return ResponseEntity.ok().body(evaluations);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<EvaluationResponseDTO> findById(@PathVariable Long id)
    {
        EvaluationResponseDTO responseDTO = evaluationService.findById(id);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping
    public ResponseEntity<EvaluationResponseDTO> create(
            @RequestBody EvaluationRequestDTO evaluationRequestDTO,
            Authentication authentication
    ) throws URISyntaxException
    {
        EvaluationResponseDTO responseDTO = evaluationService.create(evaluationRequestDTO, authentication);
        return ResponseEntity.created(new URI("/evaluations/" + responseDTO.id())).body(responseDTO);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<EvaluationResponseDTO> update(@PathVariable Long id, @RequestBody EvaluationRequestDTO requestDTO)
    {
        EvaluationResponseDTO responseDTO = evaluationService.update(id, requestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        evaluationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
