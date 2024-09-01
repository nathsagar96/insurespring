package dev.sagar.insurance.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ClientMapperTest {

    @InjectMocks
    private ClientMapper clientMapper;

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
    void toDto_shouldConvertEntityToDto() {
        ClientDTO dto = clientMapper.toDto(client);

        assertEquals(client.getId(), dto.id());
        assertEquals(client.getName(), dto.name());
        assertEquals(client.getDateOfBirth(), dto.dateOfBirth());
        assertEquals(client.getAddress(), dto.address());
        assertEquals(client.getContactInformation(), dto.contactInformation());
    }

    @Test
    void toEntity_shouldConvertDtoToEntity() {
        Client entity = clientMapper.toEntity(clientDTO);

        assertEquals(clientDTO.id(), entity.getId());
        assertEquals(clientDTO.name(), entity.getName());
        assertEquals(clientDTO.dateOfBirth(), entity.getDateOfBirth());
        assertEquals(clientDTO.address(), entity.getAddress());
        assertEquals(clientDTO.contactInformation(), entity.getContactInformation());
    }
}
