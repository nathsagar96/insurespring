package dev.sagar.insurance.mapper;

import dev.sagar.insurance.dto.ClaimDTO;
import dev.sagar.insurance.entity.Claim;
import dev.sagar.insurance.entity.Policy;
import dev.sagar.insurance.exception.ResourceNotFoundException;
import dev.sagar.insurance.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaimMapperTest {

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private ClaimMapper claimMapper;

    private Claim claim;
    private ClaimDTO claimDTO;
    private Policy policy;

    @BeforeEach
    void setUp() {
        policy = new Policy();
        policy.setId(1L);

        claim = new Claim();
        claim.setId(1L);
        claim.setClaimNumber("CLAIM123");
        claim.setDescription("Car accident claim");
        claim.setClaimDate(LocalDate.of(2023, 8, 1));
        claim.setStatus("Pending");
        claim.setPolicy(policy);

        claimDTO = new ClaimDTO(1L, "CLAIM123", "Car accident claim", LocalDate.of(2023, 8, 1), "Pending", 1L);
    }

    @Test
    void toDto_shouldConvertEntityToDto() {
        ClaimDTO dto = claimMapper.toDto(claim);

        assertEquals(claim.getId(), dto.id());
        assertEquals(claim.getClaimNumber(), dto.claimNumber());
        assertEquals(claim.getDescription(), dto.description());
        assertEquals(claim.getClaimDate(), dto.claimDate());
        assertEquals(claim.getStatus(), dto.status());
        assertEquals(claim.getPolicy().getId(), dto.policyId());
    }

    @Test
    void toEntity_shouldConvertDtoToEntity() {
        when(policyRepository.findById(claimDTO.policyId())).thenReturn(Optional.of(policy));

        Claim entity = claimMapper.toEntity(claimDTO);

        assertEquals(claimDTO.id(), entity.getId());
        assertEquals(claimDTO.claimNumber(), entity.getClaimNumber());
        assertEquals(claimDTO.description(), entity.getDescription());
        assertEquals(claimDTO.claimDate(), entity.getClaimDate());
        assertEquals(claimDTO.status(), entity.getStatus());
        assertEquals(claimDTO.policyId(), entity.getPolicy().getId());
    }

    @Test
    void toEntity_shouldThrowExceptionWhenPolicyNotFound() {
        when(policyRepository.findById(claimDTO.policyId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> claimMapper.toEntity(claimDTO));
    }
}
