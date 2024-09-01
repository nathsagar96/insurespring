package dev.sagar.insurance.client;

import dev.sagar.insurance.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service class provides methods for managing client data.
 * It interacts with the {@link ClientRepository} to perform CRUD operations on client entities.
 * The client data is transformed into {@link ClientDTO} objects using the {@link ClientMapper}.
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    /**
     * Retrieves all clients from the database.
     *
     * @return a list of {@link ClientDTO} objects representing all clients.
     */
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream().map(clientMapper::toDto).toList();
    }

    /**
     * Retrieves a client by their unique identifier.
     *
     * @param id the unique identifier of the client.
     * @return a {@link ClientDTO} object representing the client.
     * @throws ResourceNotFoundException if a client with the given id is not found.
     */
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        return clientMapper.toDto(client);
    }

    /**
     * Creates a new client in the database.
     *
     * @param clientDTO a {@link ClientDTO} object containing the client data.
     * @return a {@link ClientDTO} object representing the newly created client.
     */
    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = clientMapper.toEntity(clientDTO);
        return clientMapper.toDto(clientRepository.save(client));
    }

    /**
     * Updates an existing client in the database.
     *
     * @param id        the unique identifier of the client to update.
     * @param clientDTO a {@link ClientDTO} object containing the updated client data.
     * @return a {@link ClientDTO} object representing the updated client.
     * @throws ResourceNotFoundException if a client with the given id is not found.
     */
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Client existingClient = clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        existingClient.setName(clientDTO.name());
        existingClient.setDateOfBirth(clientDTO.dateOfBirth());
        existingClient.setAddress(clientDTO.address());
        existingClient.setContactInformation(clientDTO.contactInformation());

        return clientMapper.toDto(clientRepository.save(existingClient));
    }

    /**
     * Deletes a client from the database.
     *
     * @param id the unique identifier of the client to delete.
     * @throws ResourceNotFoundException if a client with the given id is not found.
     */
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        clientRepository.delete(client);
    }
}
