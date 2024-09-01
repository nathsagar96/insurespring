package dev.sagar.insurance.client;

import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientDTO toDto(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getName(),
                client.getDateOfBirth(),
                client.getAddress(),
                client.getContactInformation()
        );
    }

    public Client toEntity(ClientDTO clientDTO) {
        Client client = new Client();
        client.setId(clientDTO.id());
        client.setName(clientDTO.name());
        client.setDateOfBirth(clientDTO.dateOfBirth());
        client.setAddress(clientDTO.address());
        client.setContactInformation(clientDTO.contactInformation());
        return client;
    }
}