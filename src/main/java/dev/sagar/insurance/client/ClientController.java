package dev.sagar.insurance.client;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class is responsible for handling HTTP requests related to clients.
 * It provides endpoints for retrieving, creating, updating, and deleting client data.
 *
 * @author nathsagar96
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    /**
     * Retrieves a list of all clients.
     *
     * @return A ResponseEntity containing a list of ClientDTO objects and a status code of OK (200).
     */
    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return new ResponseEntity<>(clientService.getAllClients(), HttpStatus.OK);
    }

    /**
     * Retrieves a client by its unique identifier.
     *
     * @param id The unique identifier of the client.
     * @return A ResponseEntity containing the requested ClientDTO object and a status code of OK (200).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        return new ResponseEntity<>(clientService.getClientById(id), HttpStatus.OK);
    }

    /**
     * Creates a new client.
     *
     * @param clientDTO The ClientDTO object representing the new client.
     * @return A ResponseEntity containing the created ClientDTO object and a status code of CREATED (201).
     */
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        return new ResponseEntity<>(clientService.createClient(clientDTO), HttpStatus.CREATED);
    }

    /**
     * Updates an existing client.
     *
     * @param id        The unique identifier of the client to be updated.
     * @param clientDTO The updated ClientDTO object.
     * @return A ResponseEntity containing the updated ClientDTO object and a status code of OK (200).
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDTO clientDTO) {
        return new ResponseEntity<>(clientService.updateClient(id, clientDTO), HttpStatus.OK);
    }

    /**
     * Deletes a client by its unique identifier.
     *
     * @param id The unique identifier of the client to be deleted.
     * @return A ResponseEntity with a status code of NO_CONTENT (204) if the client is successfully deleted.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}