package dev.sagar.insurance.controller;

import dev.sagar.insurance.dto.PolicyDTO;
import dev.sagar.insurance.exception.GlobalExceptionHandler;
import dev.sagar.insurance.exception.ResourceNotFoundException;
import dev.sagar.insurance.service.PolicyService;
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

import java.math.BigDecimal;
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
class PolicyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PolicyService policyService;

    @InjectMocks
    private PolicyController policyController;

    private PolicyDTO policyDTO1;
    private PolicyDTO policyDTO2;

    @BeforeEach
    void setUp() {
        policyDTO1 = new PolicyDTO(1L, "POL123", "Health", new BigDecimal("50000.00"), new BigDecimal("500.00"),
                LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1), 1L);
        policyDTO2 = new PolicyDTO(2L, "POL456", "Car", new BigDecimal("70000.00"), new BigDecimal("700.00"),
                LocalDate.of(2023, 5, 1), LocalDate.of(2024, 5, 1), 2L);
        mockMvc = MockMvcBuilders
                .standaloneSetup(policyController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllPolicies_shouldReturnListOfPolicies() throws Exception {
        List<PolicyDTO> policies = Arrays.asList(policyDTO1, policyDTO2);
        when(policyService.getAllPolicies()).thenReturn(policies);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].policyNumber").value("POL123"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].policyNumber").value("POL456"))
                .andDo(print());
    }

    @Test
    void getAllPolicies_shouldReturnEmptyList() throws Exception {
        when(policyService.getAllPolicies()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0))
                .andDo(print());
    }

    @Test
    void getPolicyById_shouldReturnPolicy() throws Exception {
        when(policyService.getPolicyById(1L)).thenReturn(policyDTO1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/policies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.policyNumber").value("POL123"))
                .andExpect(jsonPath("$.type").value("Health"))
                .andExpect(jsonPath("$.premium").value(500.00))
                .andDo(print());
    }

    @Test
    void getPolicyById_shouldReturnNotFoundWhenPolicyDoesNotExist() throws Exception {
        when(policyService.getPolicyById(anyLong())).thenThrow(new ResourceNotFoundException("Policy not found with id: 1"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/policies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Policy not found with id: 1"))
                .andDo(print());
    }

    @Test
    void createPolicy_shouldReturnCreatedPolicy() throws Exception {
        when(policyService.createPolicy(any(PolicyDTO.class))).thenReturn(policyDTO1);

        String policyJson = """
                {
                    "policyNumber": "POL123",
                    "type": "Health",
                    "coverageAmount": 50000.00,
                    "premium": 500.00,
                    "startDate": "2023-01-01",
                    "endDate": "2024-01-01",
                    "clientId": 1
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(policyJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.policyNumber").value("POL123"))
                .andExpect(jsonPath("$.type").value("Health"))
                .andDo(print());
    }


    @Test
    void updatePolicy_shouldReturnUpdatedPolicy() throws Exception {
        when(policyService.updatePolicy(anyLong(), any(PolicyDTO.class))).thenReturn(policyDTO2);

        String updatedPolicyJson = """
                {
                    "policyNumber": "POL456",
                    "type": "Car",
                    "coverageAmount": 70000.00,
                    "premium": 700.00,
                    "startDate": "2023-05-01",
                    "endDate": "2024-05-01",
                    "clientId": 2
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/policies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPolicyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.policyNumber").value("POL456"))
                .andExpect(jsonPath("$.type").value("Car"))
                .andDo(print());
    }

    @Test
    void updatePolicy_shouldReturnNotFoundWhenPolicyDoesNotExist() throws Exception {
        when(policyService.updatePolicy(anyLong(), any(PolicyDTO.class))).thenThrow(new ResourceNotFoundException("Policy not found with id: 1"));

        String updatedPolicyJson = """
                {
                    "policyNumber": "POL456",
                    "type": "Car",
                    "coverageAmount": 70000.00,
                    "premium": 700.00,
                    "startDate": "2023-05-01",
                    "endDate": "2024-05-01",
                    "clientId": 2
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/policies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPolicyJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Policy not found with id: 1"))
                .andDo(print());
    }

    @Test
    void deletePolicy_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(policyService).deletePolicy(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/policies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void deletePolicy_shouldReturnNotFoundWhenPolicyDoesNotExist() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Policy not found with id: 1")).when(policyService).deletePolicy(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/policies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Policy not found with id: 1"))
                .andDo(print());
    }
}
