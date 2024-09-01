package dev.sagar.insurance.policy;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PolicyDTO(

        Long id,

        @NotBlank(message = "Policy Number is mandatory")
        @Size(min = 5, max = 20, message = "Policy Number must be between 5 and 20 characters")
        String policyNumber,

        @NotBlank(message = "Policy Type is mandatory")
        String type,

        @NotNull(message = "Coverage Amount is mandatory")
        @DecimalMin(value = "1000.00", message = "Coverage Amount must be at least 1000.00")
        BigDecimal coverageAmount,

        @NotNull(message = "Premium is mandatory")
        @DecimalMin(value = "100.00", message = "Premium must be at least 100.00")
        BigDecimal premium,

        @NotNull(message = "Start Date is mandatory")
        @PastOrPresent(message = "Start Date must be in the past or present")
        LocalDate startDate,

        @NotNull(message = "End Date is mandatory")
        LocalDate endDate,

        @NotNull(message = "Client ID is mandatory")
        Long clientId
) {
}