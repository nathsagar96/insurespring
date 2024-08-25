package dev.sagar.insurance.service;

import dev.sagar.insurance.dto.ClaimDTO;
import dev.sagar.insurance.entity.Claim;
import dev.sagar.insurance.exception.ResourceNotFoundException;
import dev.sagar.insurance.mapper.ClaimMapper;
import dev.sagar.insurance.repository.ClaimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private ClaimMapper claimMapper;

    @InjectMocks
    private ClaimService claimService;

    private Claim claim;
    private ClaimDTO claimDTO;

    @BeforeEach
    void setUp() {
        claim = new Claim();
        claim.setId(1L);
        claim.setClaimNumber("CLAIM123");
        claim.setDescription("Car accident claim");
        claim.setClaimDate(LocalDate.of(2023, 8, 1));
        claim.setStatus("Pending");

        claimDTO = new ClaimDTO(1L, "CLAIM123", "Car accident claim", LocalDate.of(2023, 8, 1), "Pending", 1L);
    }

    @Test
    void getAllClaims_shouldReturnListOfClaimDTOs() {
        when(claimRepository.findAll()).thenReturn(List.of(claim));
        when(claimMapper.toDto(claim)).thenReturn(claimDTO);

        List<ClaimDTO> result = claimService.getAllClaims();

        assertEquals(1, result.size());
        assertEquals(claimDTO, result.get(0));
    }

    @Test
    void getClaimById_shouldReturnClaimDTOWhenFound() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        when(claimMapper.toDto(claim)).thenReturn(claimDTO);

        ClaimDTO result = claimService.getClaimById(1L);

        assertEquals(claimDTO, result);
    }

    @Test
    void getClaimById_shouldThrowExceptionWhenNotFound() {
        when(claimRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> claimService.getClaimById(1L));
    }

    @Test
    void createClaim_shouldReturnCreatedClaimDTO() {
        when(claimMapper.toEntity(claimDTO)).thenReturn(claim);
        when(claimRepository.save(claim)).thenReturn(claim);
        when(claimMapper.toDto(claim)).thenReturn(claimDTO);

        ClaimDTO result = claimService.createClaim(claimDTO);

        assertEquals(claimDTO, result);
    }

    @Test
    void updateClaim_shouldReturnUpdatedClaimDTO() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        when(claimRepository.save(claim)).thenReturn(claim);
        when(claimMapper.toDto(claim)).thenReturn(claimDTO);

        ClaimDTO result = claimService.updateClaim(1L, claimDTO);

        assertEquals(claimDTO, result);
    }

    @Test
    void updateClaim_shouldThrowExceptionWhenNotFound() {
        when(claimRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> claimService.updateClaim(1L, claimDTO));
    }

    @Test
    void deleteClaim_shouldDeleteClaimWhenFound() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        claimService.deleteClaim(1L);

        verify(claimRepository, times(1)).delete(claim);
    }

    @Test
    void deleteClaim_shouldThrowExceptionWhenNotFound() {
        when(claimRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> claimService.deleteClaim(1L));
    }
}
