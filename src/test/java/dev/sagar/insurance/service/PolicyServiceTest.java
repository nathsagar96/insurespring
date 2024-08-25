package dev.sagar.insurance.service;

import dev.sagar.insurance.dto.PolicyDTO;
import dev.sagar.insurance.entity.Policy;
import dev.sagar.insurance.exception.ResourceNotFoundException;
import dev.sagar.insurance.mapper.PolicyMapper;
import dev.sagar.insurance.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private PolicyMapper policyMapper;

    @InjectMocks
    private PolicyService policyService;

    private Policy policy;
    private PolicyDTO policyDTO;

    @BeforeEach
    void setUp() {
        policy = new Policy();
        policy.setId(1L);
        policy.setPolicyNumber("POL123");
        policy.setType("Health");
        policy.setCoverageAmount(new BigDecimal("50000.00"));
        policy.setPremium(new BigDecimal("500.00"));
        policy.setStartDate(LocalDate.of(2023, 1, 1));
        policy.setEndDate(LocalDate.of(2024, 1, 1));

        policyDTO = new PolicyDTO(1L, "POL123", "Health", new BigDecimal("50000.00"), new BigDecimal("500.00"),
                LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1), 1L);
    }

    @Test
    void getAllPolicies_shouldReturnListOfPolicyDTOs() {
        when(policyRepository.findAll()).thenReturn(List.of(policy));
        when(policyMapper.toDto(policy)).thenReturn(policyDTO);

        List<PolicyDTO> result = policyService.getAllPolicies();

        assertEquals(1, result.size());
        assertEquals(policyDTO, result.get(0));
    }

    @Test
    void getPolicyById_shouldReturnPolicyDTOWhenFound() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(policyMapper.toDto(policy)).thenReturn(policyDTO);

        PolicyDTO result = policyService.getPolicyById(1L);

        assertEquals(policyDTO, result);
    }

    @Test
    void getPolicyById_shouldThrowExceptionWhenNotFound() {
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> policyService.getPolicyById(1L));
    }

    @Test
    void createPolicy_shouldReturnCreatedPolicyDTO() {
        when(policyMapper.toEntity(policyDTO)).thenReturn(policy);
        when(policyRepository.save(policy)).thenReturn(policy);
        when(policyMapper.toDto(policy)).thenReturn(policyDTO);

        PolicyDTO result = policyService.createPolicy(policyDTO);

        assertEquals(policyDTO, result);
    }

    @Test
    void updatePolicy_shouldReturnUpdatedPolicyDTO() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(policyRepository.save(policy)).thenReturn(policy);
        when(policyMapper.toDto(policy)).thenReturn(policyDTO);

        PolicyDTO result = policyService.updatePolicy(1L, policyDTO);

        assertEquals(policyDTO, result);
    }

    @Test
    void updatePolicy_shouldThrowExceptionWhenNotFound() {
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> policyService.updatePolicy(1L, policyDTO));
    }

    @Test
    void deletePolicy_shouldDeletePolicyWhenFound() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));

        policyService.deletePolicy(1L);

        verify(policyRepository, times(1)).delete(policy);
    }

    @Test
    void deletePolicy_shouldThrowExceptionWhenNotFound() {
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> policyService.deletePolicy(1L));
    }
}
