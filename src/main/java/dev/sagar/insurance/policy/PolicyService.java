package dev.sagar.insurance.policy;

import dev.sagar.insurance.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyMapper policyMapper;

    public List<PolicyDTO> getAllPolicies() {
        return policyRepository.findAll().stream().map(policyMapper::toDto).toList();
    }

    public PolicyDTO getPolicyById(Long id) {
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        return policyMapper.toDto(policy);
    }


    public PolicyDTO createPolicy(PolicyDTO policyDTO) {
        Policy policy = policyMapper.toEntity(policyDTO);
        return policyMapper.toDto(policyRepository.save(policy));
    }

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

    public void deletePolicy(Long id) {
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        policyRepository.delete(policy);
    }
}