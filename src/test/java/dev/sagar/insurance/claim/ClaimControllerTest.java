package dev.sagar.insurance.claim;

import dev.sagar.insurance.exception.GlobalExceptionHandler;
import dev.sagar.insurance.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClaimControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClaimService claimService;

    @InjectMocks
    private ClaimController claimController;

    private ClaimDTO claimDTO1;
    private ClaimDTO claimDTO2;

    @BeforeEach
    void setUp() {
        claimDTO1 = new ClaimDTO(1L, "CLM123", "Description 1", LocalDate.of(2023, 1, 1), "OPEN", 101L);
        claimDTO2 = new ClaimDTO(2L, "CLM456", "Description 2", LocalDate.of(2023, 5, 1), "CLOSED", 102L);
        mockMvc = MockMvcBuilders
                .standaloneSetup(claimController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllClaims_shouldReturnListOfClaims() throws Exception {
        List<ClaimDTO> claims = Arrays.asList(claimDTO1, claimDTO2);
        when(claimService.getAllClaims()).thenReturn(claims);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/claims")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].claimNumber").value("CLM123"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].claimNumber").value("CLM456"))
                .andDo(print());
    }

    @Test
    void getAllClaims_shouldReturnEmptyList() throws Exception {
        when(claimService.getAllClaims()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/claims")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0))
                .andDo(print());
    }

    @Test
    void getClaimById_shouldReturnClaim() throws Exception {
        when(claimService.getClaimById(1L)).thenReturn(claimDTO1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/claims/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.claimNumber").value("CLM123"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.policyId").value(101L))
                .andDo(print());
    }

    @Test
    void getClaimById_shouldReturnNotFound() throws Exception {
        when(claimService.getClaimById(anyLong())).thenThrow(new ResourceNotFoundException("Claim not found with id: 1"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/claims/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Claim not found with id: 1"))
                .andDo(print());
    }

    @Test
    void createClaim_shouldReturnCreatedClaim() throws Exception {
        when(claimService.createClaim(any(ClaimDTO.class))).thenReturn(claimDTO1);

        String claimJson = """
                {
                    "claimNumber": "CLM123",
                    "description": "Description 1",
                    "claimDate": "2023-01-01",
                    "status": "OPEN",
                    "policyId": 101
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(claimJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.claimNumber").value("CLM123"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.policyId").value(101L))
                .andDo(print());
    }

    @Test
    void createClaim_shouldReturnBadRequestWhenMissingArguments() throws Exception {
        String incompleteClaimJson = """
                {
                    "description": "Description 1",
                    "claimDate": "2023-01-01",
                    "status": "OPEN"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompleteClaimJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andDo(print());
    }

    @Test
    void updateClaim_shouldReturnUpdatedClaim() throws Exception {
        when(claimService.updateClaim(anyLong(), any(ClaimDTO.class))).thenReturn(claimDTO2);

        String updatedClaimJson = """
                {
                    "claimNumber": "CLM456",
                    "description": "Updated Description",
                    "claimDate": "2023-05-01",
                    "status": "CLOSED",
                    "policyId": 102
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/claims/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClaimJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.claimNumber").value("CLM456"))
                .andExpect(jsonPath("$.description").value("Description 2"))
                .andExpect(jsonPath("$.status").value("CLOSED"))
                .andExpect(jsonPath("$.policyId").value(102L))
                .andDo(print());
    }

    @Test
    void updateClaim_shouldReturnBadRequestWhenMissingArguments() throws Exception {
        String incompleteUpdateClaimJson = """
                {
                    "claimNumber": "CLM456",
                    "claimDate": "2023-05-01",
                    "status": "CLOSED"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/claims/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompleteUpdateClaimJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andDo(print());
    }

    @Test
    void updateClaim_shouldReturnNotFoundWhenClaimDoesNotExist() throws Exception {
        when(claimService.updateClaim(anyLong(), any(ClaimDTO.class)))
                .thenThrow(new ResourceNotFoundException("Claim not found with id: 1"));

        String updateClaimJson = """
                {
                    "claimNumber": "CLM456",
                    "description": "Updated Description",
                    "claimDate": "2023-05-01",
                    "status": "CLOSED",
                    "policyId": 102
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/claims/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateClaimJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Claim not found with id: 1"))
                .andDo(print());
    }

    @Test
    void deleteClaim_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(claimService).deleteClaim(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/claims/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void deleteClaim_shouldReturnNotFoundWhenClaimDoesNotExist() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Claim not found with id: 1")).when(claimService).deleteClaim(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/claims/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Claim not found with id: 1"))
                .andDo(print());
    }
}
