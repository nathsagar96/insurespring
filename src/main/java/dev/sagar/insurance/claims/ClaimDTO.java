package dev.sagar.insurance.claims;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record ClaimDTO(
        Long id,

        @NotBlank(message = "Claim Number is mandatory")
        String claimNumber,

        @NotBlank(message = "Description is mandatory")
        String description,

        @NotNull(message = "Claim Date is mandatory")
        @PastOrPresent(message = "Claim Date must be in the past or present")
        LocalDate claimDate,

        String status,

        @NotNull(message = "Policy ID is mandatory")
        Long policyId
) {
}