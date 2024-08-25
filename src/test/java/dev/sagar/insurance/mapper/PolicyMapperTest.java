package dev.sagar.insurance.mapper;

import dev.sagar.insurance.dto.PolicyDTO;
import dev.sagar.insurance.entity.Client;
import dev.sagar.insurance.entity.Policy;
import dev.sagar.insurance.exception.ResourceNotFoundException;
import dev.sagar.insurance.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolicyMapperTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private PolicyMapper policyMapper;

    private Policy policy;
    private PolicyDTO policyDTO;
    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);

        policy = new Policy();
        policy.setId(1L);
        policy.setPolicyNumber("POL123");
        policy.setType("Health");
        policy.setCoverageAmount(new BigDecimal("50000.00"));
        policy.setPremium(new BigDecimal("500.00"));
        policy.setStartDate(LocalDate.of(2023, 1, 1));
        policy.setEndDate(LocalDate.of(2024, 1, 1));
        policy.setClient(client);

        policyDTO = new PolicyDTO(1L, "POL123", "Health", new BigDecimal("50000.00"), new BigDecimal("500.00"),
                LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1), 1L);
    }

    @Test
    void toDto_shouldConvertEntityToDto() {
        PolicyDTO dto = policyMapper.toDto(policy);

        assertEquals(policy.getId(), dto.id());
        assertEquals(policy.getPolicyNumber(), dto.policyNumber());
        assertEquals(policy.getType(), dto.type());
        assertEquals(policy.getCoverageAmount(), dto.coverageAmount());
        assertEquals(policy.getPremium(), dto.premium());
        assertEquals(policy.getStartDate(), dto.startDate());
        assertEquals(policy.getEndDate(), dto.endDate());
        assertEquals(policy.getClient().getId(), dto.clientId());
    }

    @Test
    void toEntity_shouldConvertDtoToEntity() {
        when(clientRepository.findById(policyDTO.clientId())).thenReturn(Optional.of(client));

        Policy entity = policyMapper.toEntity(policyDTO);

        assertEquals(policyDTO.id(), entity.getId());
        assertEquals(policyDTO.policyNumber(), entity.getPolicyNumber());
        assertEquals(policyDTO.type(), entity.getType());
        assertEquals(policyDTO.coverageAmount(), entity.getCoverageAmount());
        assertEquals(policyDTO.premium(), entity.getPremium());
        assertEquals(policyDTO.startDate(), entity.getStartDate());
        assertEquals(policyDTO.endDate(), entity.getEndDate());
        assertEquals(policyDTO.clientId(), entity.getClient().getId());
    }

    @Test
    void toEntity_shouldThrowExceptionWhenClientNotFound() {
        when(clientRepository.findById(policyDTO.clientId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> policyMapper.toEntity(policyDTO));
    }
}
