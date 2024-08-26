package dev.sagar.insurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClientDTO(

        Long id,

        @NotBlank(message = "Name is mandatory")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @NotNull(message = "Date of Birth is mandatory")
        @Past(message = "Date of Birth must be in the past")
        LocalDate dateOfBirth,

        @NotBlank(message = "Address is mandatory")
        String address,

        @NotBlank(message = "Contact Information is mandatory")
        @Size(min = 10, max = 15, message = "Contact Information must be between 10 and 15 characters")
        String contactInformation
) {
}