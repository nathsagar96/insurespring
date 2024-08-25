package dev.sagar.insurance.service;

import dev.sagar.insurance.dto.ClientDTO;
import dev.sagar.insurance.entity.Client;
import dev.sagar.insurance.exception.ResourceNotFoundException;
import dev.sagar.insurance.mapper.ClientMapper;
import dev.sagar.insurance.repository.ClientRepository;
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
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setDateOfBirth(LocalDate.of(1990, 1, 1));
        client.setAddress("123 Main St");
        client.setContactInformation("9876543210");

        clientDTO = new ClientDTO(1L, "John Doe", LocalDate.of(1990, 1, 1), "123 Main St", "9876543210");
    }

    @Test
    void getAllClients_shouldReturnListOfClientDTOs() {
        when(clientRepository.findAll()).thenReturn(List.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientDTO);

        List<ClientDTO> result = clientService.getAllClients();

        assertEquals(1, result.size());
        assertEquals(clientDTO, result.get(0));
    }

    @Test
    void getClientById_shouldReturnClientDTOWhenFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientDTO);

        ClientDTO result = clientService.getClientById(1L);

        assertEquals(clientDTO, result);
    }

    @Test
    void getClientById_shouldThrowExceptionWhenNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(1L));
    }

    @Test
    void createClient_shouldReturnCreatedClientDTO() {
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(clientDTO);

        ClientDTO result = clientService.createClient(clientDTO);

        assertEquals(clientDTO, result);
    }

    @Test
    void updateClient_shouldReturnUpdatedClientDTO() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(clientDTO);

        ClientDTO result = clientService.updateClient(1L, clientDTO);

        assertEquals(clientDTO, result);
    }

    @Test
    void updateClient_shouldThrowExceptionWhenNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.updateClient(1L, clientDTO));
    }

    @Test
    void deleteClient_shouldDeleteClientWhenFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.deleteClient(1L);

        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    void deleteClient_shouldThrowExceptionWhenNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.deleteClient(1L));
    }
}
