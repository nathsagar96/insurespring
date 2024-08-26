package dev.sagar.insurance.service;

import dev.sagar.insurance.dto.ClaimDTO;
import dev.sagar.insurance.entity.Claim;
import dev.sagar.insurance.exception.ResourceNotFoundException;
import dev.sagar.insurance.mapper.ClaimMapper;
import dev.sagar.insurance.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ClaimMapper claimMapper;

    public List<ClaimDTO> getAllClaims() {
        return claimRepository.findAll().stream().map(claimMapper::toDto).toList();
    }

    public ClaimDTO getClaimById(Long id) {
        Claim claim = claimRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
        return claimMapper.toDto(claim);
    }

    public ClaimDTO createClaim(ClaimDTO claimDTO) {
        Claim claim = claimMapper.toEntity(claimDTO);
        return claimMapper.toDto(claimRepository.save(claim));
    }

    public ClaimDTO updateClaim(Long id, ClaimDTO claimDTO) {
        Claim existingClaim = claimRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));

        existingClaim.setClaimNumber(claimDTO.claimNumber());
        existingClaim.setDescription(claimDTO.description());
        existingClaim.setClaimDate(claimDTO.claimDate());
        existingClaim.setStatus(claimDTO.status());

        return claimMapper.toDto(claimRepository.save(existingClaim));
    }


    public void deleteClaim(Long id) {
        Claim claim = claimRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
        claimRepository.delete(claim);
    }
}