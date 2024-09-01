package dev.sagar.insurance.claim;

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
 * This class is responsible for handling HTTP requests related to claims.
 * It provides endpoints for retrieving, creating, updating, and deleting claims.
 *
 * @author nathsagar96
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    /**
     * Retrieves all claims.
     *
     * @return A ResponseEntity containing a list of ClaimDTO objects and a status code of OK (200).
     */
    @GetMapping
    public ResponseEntity<List<ClaimDTO>> getAllClaims() {
        return new ResponseEntity<>(claimService.getAllClaims(), HttpStatus.OK);
    }

    /**
     * Retrieves a claim by its ID.
     *
     * @param id The ID of the claim to retrieve.
     * @return A ResponseEntity containing the requested ClaimDTO object and a status code of OK (200).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClaimDTO> getClaimById(@PathVariable Long id) {
        return new ResponseEntity<>(claimService.getClaimById(id), HttpStatus.OK);
    }

    /**
     * Creates a new claim.
     *
     * @param claimDTO The claim data to create.
     * @return A ResponseEntity containing the created ClaimDTO object and a status code of CREATED (201).
     */
    @PostMapping
    public ResponseEntity<ClaimDTO> createClaim(@Valid @RequestBody ClaimDTO claimDTO) {
        return new ResponseEntity<>(claimService.createClaim(claimDTO), HttpStatus.CREATED);
    }

    /**
     * Updates an existing claim.
     *
     * @param id       The ID of the claim to update.
     * @param claimDTO The updated claim data.
     * @return A ResponseEntity containing the updated ClaimDTO object and a status code of OK (200).
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClaimDTO> updateClaim(@PathVariable Long id, @Valid @RequestBody ClaimDTO claimDTO) {
        return new ResponseEntity<>(claimService.updateClaim(id, claimDTO), HttpStatus.OK);
    }

    /**
     * Deletes a claim by its ID.
     *
     * @param id The ID of the claim to delete.
     * @return A ResponseEntity with a status code of NO_CONTENT (204) if the claim is successfully deleted.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}