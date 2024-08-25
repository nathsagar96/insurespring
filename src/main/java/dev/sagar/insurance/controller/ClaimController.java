package dev.sagar.insurance.controller;

import dev.sagar.insurance.dto.ClaimDTO;
import dev.sagar.insurance.service.ClaimService;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping
    public ResponseEntity<List<ClaimDTO>> getAllClaims() {
        return new ResponseEntity<>(claimService.getAllClaims(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimDTO> getClaimById(@PathVariable Long id) {
        return new ResponseEntity<>(claimService.getClaimById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ClaimDTO> createClaim(@Valid @RequestBody ClaimDTO claimDTO) {
        return new ResponseEntity<>(claimService.createClaim(claimDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClaimDTO> updateClaim(@PathVariable Long id, @Valid @RequestBody ClaimDTO claimDTO) {
        return new ResponseEntity<>(claimService.updateClaim(id, claimDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}