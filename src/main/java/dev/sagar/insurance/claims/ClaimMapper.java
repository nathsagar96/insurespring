package dev.sagar.insurance.claims;

import dev.sagar.insurance.exception.ResourceNotFoundException;
import dev.sagar.insurance.policies.Policy;
import dev.sagar.insurance.policies.PolicyRepository;
import org.springframework.stereotype.Component;

@Component
public class ClaimMapper {

    private final PolicyRepository policyRepository;

    public ClaimMapper(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public ClaimDTO toDto(Claim claim) {
        return new ClaimDTO(
                claim.getId(),
                claim.getClaimNumber(),
                claim.getDescription(),
                claim.getClaimDate(),
                claim.getStatus(),
                claim.getPolicy().getId()
        );
    }

    public Claim toEntity(ClaimDTO claimDTO) {
        Claim claim = new Claim();
        claim.setId(claimDTO.id());
        claim.setClaimNumber(claimDTO.claimNumber());
        claim.setDescription(claimDTO.description());
        claim.setClaimDate(claimDTO.claimDate());
        claim.setStatus(claimDTO.status());

        // Fetch the associated Policy entity
        Policy policy = policyRepository.findById(claimDTO.policyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with ID: " + claimDTO.policyId()));
        claim.setPolicy(policy);

        return claim;
    }
}