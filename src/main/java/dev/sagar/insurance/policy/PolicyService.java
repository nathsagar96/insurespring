package dev.sagar.insurance.policy;

import dev.sagar.insurance.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class provides services related to insurance policies.
 * It interacts with the {@link PolicyRepository} to perform CRUD operations on policies.
 * It also uses {@link PolicyMapper} to convert between {@link Policy} and {@link PolicyDTO}.
 */
@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyMapper policyMapper;

    /**
     * Retrieves all policies from the database.
     *
     * @return a list of {@link PolicyDTO} representing all policies.
     */
    public List<PolicyDTO> getAllPolicies() {
        return policyRepository.findAll().stream().map(policyMapper::toDto).toList();
    }

    /**
     * Retrieves a policy by its ID.
     *
     * @param id the ID of the policy to retrieve.
     * @return a {@link PolicyDTO} representing the policy with the given ID.
     * @throws ResourceNotFoundException if no policy is found with the given ID.
     */
    public PolicyDTO getPolicyById(Long id) {
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        return policyMapper.toDto(policy);
    }

    /**
     * Creates a new policy in the database.
     *
     * @param policyDTO the {@link PolicyDTO} representing the policy to create.
     * @return a {@link PolicyDTO} representing the newly created policy.
     */
    public PolicyDTO createPolicy(PolicyDTO policyDTO) {
        Policy policy = policyMapper.toEntity(policyDTO);
        return policyMapper.toDto(policyRepository.save(policy));
    }

    /**
     * Updates an existing policy in the database.
     *
     * @param id        the ID of the policy to update.
     * @param policyDTO the {@link PolicyDTO} representing the updated policy.
     * @return a {@link PolicyDTO} representing the updated policy.
     * @throws ResourceNotFoundException if no policy is found with the given ID.
     */
    public PolicyDTO updatePolicy(Long id, PolicyDTO policyDTO) {
        Policy existingPolicy = policyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));

        existingPolicy.setPolicyNumber(policyDTO.policyNumber());
        existingPolicy.setType(policyDTO.type());
        existingPolicy.setCoverageAmount(policyDTO.coverageAmount());
        existingPolicy.setPremium(policyDTO.premium());
        existingPolicy.setStartDate(policyDTO.startDate());
        existingPolicy.setEndDate(policyDTO.endDate());

        return policyMapper.toDto(policyRepository.save(existingPolicy));
    }

    /**
     * Deletes a policy from the database.
     *
     * @param id the ID of the policy to delete.
     * @throws ResourceNotFoundException if no policy is found with the given ID.
     */
    public void deletePolicy(Long id) {
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        policyRepository.delete(policy);
    }
}