package com.example.pi_dev_ops_backend.services;

import com.example.pi_dev_ops_backend.domain.dtos.EvaluationRequestDTO;
import com.example.pi_dev_ops_backend.domain.dtos.EvaluationResponseDTO;
import com.example.pi_dev_ops_backend.domain.entities.ContractedListing;
import com.example.pi_dev_ops_backend.domain.entities.Evaluation;
import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import com.example.pi_dev_ops_backend.domain.mappers.EvaluationMapper;
import com.example.pi_dev_ops_backend.domain.queryParams.PaginationParams;
import com.example.pi_dev_ops_backend.repository.EvaluationRepository;
import com.example.pi_dev_ops_backend.services.exceptions.InvalidArgsException;
import com.example.pi_dev_ops_backend.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EvaluationService
{
    private final EvaluationRepository evaluationRepository;

    private final ContractedListingService contractedListingService;
    private final UserProfileService userProfileService;

    public Page<EvaluationResponseDTO> findAll(PaginationParams paginationParams)
    {
        Pageable pageable = PageRequest.of(paginationParams.getPage(), paginationParams.getSize());
        //        Specification<Evaluation> specification = EvaluationSpecification.filter(paginationParams);
        return evaluationRepository.findAll(pageable).map(EvaluationMapper.INSTANCE::toEvaluationResponseDTO);
    }

    public EvaluationResponseDTO findById(Long id)
    {
        Evaluation evaluation = findEntityById(id);
        return EvaluationMapper.INSTANCE.toEvaluationResponseDTO(evaluation);
    }

    public EvaluationResponseDTO create(EvaluationRequestDTO evaluationRequestDTO, Authentication authentication)
    {
        ContractedListing contractedListing = contractedListingService
                .findEntityById(evaluationRequestDTO.contractedListingId());

        UserProfile userProfile = userProfileService.findEntityByEmail(authentication.getName());
        if(!Objects.equals(contractedListing.getClient().getId(), userProfile.getId()))
            throw new InvalidArgsException("You can't evaluate a contract you didn't hire");

        Evaluation evaluation = new Evaluation(evaluationRequestDTO.comment(),
                evaluationRequestDTO.stars(),
                Instant.now(),
                contractedListing);

        evaluation = evaluationRepository.save(evaluation);
        return EvaluationMapper.INSTANCE.toEvaluationResponseDTO(evaluation);
    }

    public EvaluationResponseDTO update(Long id, EvaluationRequestDTO evaluationRequestDTO)
    {
        Evaluation evaluation = findEntityById(id);

        evaluation.setComment(evaluationRequestDTO.comment());
        evaluation.setStars(evaluationRequestDTO.stars());

        return EvaluationMapper.INSTANCE.toEvaluationResponseDTO(evaluationRepository.save(evaluation));
    }

    public void delete(Long id)
    {
        Evaluation evaluation = findEntityById(id);
        evaluationRepository.delete(evaluation);
    }

    Evaluation findEntityById(Long id)
    {
        return evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Evaluation.class, id));
    }

}
