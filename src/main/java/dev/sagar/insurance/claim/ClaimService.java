package dev.sagar.insurance.claim;

import dev.sagar.insurance.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service class provides methods for managing claims.
 * It interacts with the {@link ClaimRepository} to perform CRUD operations on {@link Claim} entities.
 * The {@link ClaimMapper} is used to convert between {@link Claim} and {@link ClaimDTO} objects.
 */
@Service
@RequiredArgsConstructor
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ClaimMapper claimMapper;

    /**
     * Retrieves all claims from the database.
     *
     * @return a list of {@link ClaimDTO} objects representing all claims.
     */
    public List<ClaimDTO> getAllClaims() {
        return claimRepository.findAll().stream().map(claimMapper::toDto).toList();
    }

    /**
     * Retrieves a claim by its unique identifier.
     *
     * @param id the unique identifier of the claim.
     * @return a {@link ClaimDTO} object representing the claim with the given id.
     * @throws ResourceNotFoundException if no claim is found with the given id.
     */
    public ClaimDTO getClaimById(Long id) {
        Claim claim = claimRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
        return claimMapper.toDto(claim);
    }

    /**
     * Creates a new claim in the database.
     *
     * @param claimDTO a {@link ClaimDTO} object representing the new claim.
     * @return a {@link ClaimDTO} object representing the newly created claim.
     */
    public ClaimDTO createClaim(ClaimDTO claimDTO) {
        Claim claim = claimMapper.toEntity(claimDTO);
        return claimMapper.toDto(claimRepository.save(claim));
    }

    /**
     * Updates an existing claim in the database.
     *
     * @param id       the unique identifier of the claim to be updated.
     * @param claimDTO a {@link ClaimDTO} object representing the updated claim.
     * @return a {@link ClaimDTO} object representing the updated claim.
     * @throws ResourceNotFoundException if no claim is found with the given id.
     */
    public ClaimDTO updateClaim(Long id, ClaimDTO claimDTO) {
        Claim existingClaim = claimRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));

        existingClaim.setClaimNumber(claimDTO.claimNumber());
        existingClaim.setDescription(claimDTO.description());
        existingClaim.setClaimDate(claimDTO.claimDate());
        existingClaim.setStatus(claimDTO.status());

        return claimMapper.toDto(claimRepository.save(existingClaim));
    }

    /**
     * Deletes a claim from the database.
     *
     * @param id the unique identifier of the claim to be deleted.
     * @throws ResourceNotFoundException if no claim is found with the given id.
     */
    public void deleteClaim(Long id) {
        Claim claim = claimRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
        claimRepository.delete(claim);
    }
}