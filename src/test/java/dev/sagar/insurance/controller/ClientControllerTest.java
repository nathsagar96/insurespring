package dev.sagar.insurance.controller;

import dev.sagar.insurance.dto.ClientDTO;
import dev.sagar.insurance.exception.GlobalExceptionHandler;
import dev.sagar.insurance.exception.ResourceNotFoundException;
import dev.sagar.insurance.service.ClientService;
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
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private ClientDTO clientDTO1;
    private ClientDTO clientDTO2;

    @BeforeEach
    void setUp() {
        clientDTO1 = new ClientDTO(1L, "John Doe", LocalDate.of(1990, 1, 1), "123 Main St", "9876543210");
        clientDTO2 = new ClientDTO(2L, "Jane Smith", LocalDate.of(1985, 5, 10), "456 Elm St", "9876543211");
        mockMvc = MockMvcBuilders
                .standaloneSetup(clientController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllClients_shouldReturnListOfClients() throws Exception {
        List<ClientDTO> clients = Arrays.asList(clientDTO1, clientDTO2);
        when(clientService.getAllClients()).thenReturn(clients);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
                .andDo(print());
    }

    @Test
    void getAllClients_shouldReturnEmptyList() throws Exception {
        when(clientService.getAllClients()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0))
                .andDo(print());
    }

    @Test
    void getClientById_shouldReturnClient() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(clientDTO1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.contactInformation").value("9876543210"))
                .andDo(print());
    }

    @Test
    void getClientById_shouldReturnNotFoundWhenClientDoesNotExist() throws Exception {
        when(clientService.getClientById(anyLong())).thenThrow(new ResourceNotFoundException("Client not found with id: 1"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Client not found with id: 1"))
                .andDo(print());
    }

    @Test
    void createClient_shouldReturnCreatedClient() throws Exception {
        when(clientService.createClient(any(ClientDTO.class))).thenReturn(clientDTO1);

        String clientJson = """
                {
                    "name": "John Doe",
                    "dateOfBirth": "1990-01-01",
                    "address": "123 Main St",
                    "contactInformation": "9876543210"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.contactInformation").value("9876543210"))
                .andDo(print());
    }

    @Test
    void updateClient_shouldReturnUpdatedClient() throws Exception {
        when(clientService.updateClient(anyLong(), any(ClientDTO.class))).thenReturn(clientDTO2);

        String updatedClientJson = """
                {
                    "name": "Jane Smith",
                    "dateOfBirth": "1985-05-10",
                    "address": "456 Elm St",
                    "contactInformation": "9876543211"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.address").value("456 Elm St"))
                .andExpect(jsonPath("$.contactInformation").value("9876543211"))
                .andDo(print());
    }

    @Test
    void updateClient_shouldReturnNotFoundWhenClientDoesNotExist() throws Exception {
        when(clientService.updateClient(anyLong(), any(ClientDTO.class))).thenThrow(new ResourceNotFoundException("Client not found with id: 1"));

        String updatedClientJson = """
                {
                    "name": "Jane Smith",
                    "dateOfBirth": "1985-05-10",
                    "address": "456 Elm St",
                    "contactInformation": "9876543211"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClientJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Client not found with id: 1"))
                .andDo(print());
    }

    @Test
    void deleteClient_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(clientService).deleteClient(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void deleteClient_shouldReturnNotFoundWhenClientDoesNotExist() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Client not found with id: 1")).when(clientService).deleteClient(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Client not found with id: 1"))
                .andDo(print());
    }
}
