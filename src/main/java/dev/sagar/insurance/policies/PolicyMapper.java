package dev.sagar.insurance.policies;

import dev.sagar.insurance.clients.Client;
import dev.sagar.insurance.clients.ClientRepository;
import dev.sagar.insurance.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class PolicyMapper {

    private final ClientRepository clientRepository;

    public PolicyMapper(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public PolicyDTO toDto(Policy policy) {
        return new PolicyDTO(
                policy.getId(),
                policy.getPolicyNumber(),
                policy.getType(),
                policy.getCoverageAmount(),
                policy.getPremium(),
                policy.getStartDate(),
                policy.getEndDate(),
                policy.getClient().getId()
        );
    }

    public Policy toEntity(PolicyDTO policyDTO) {
        Policy policy = new Policy();
        policy.setId(policyDTO.id());
        policy.setPolicyNumber(policyDTO.policyNumber());
        policy.setType(policyDTO.type());
        policy.setCoverageAmount(policyDTO.coverageAmount());
        policy.setPremium(policyDTO.premium());
        policy.setStartDate(policyDTO.startDate());
        policy.setEndDate(policyDTO.endDate());

        // Fetch the associated Client entity
        Client client = clientRepository.findById(policyDTO.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + policyDTO.clientId()));
        policy.setClient(client);

        return policy;
    }
}