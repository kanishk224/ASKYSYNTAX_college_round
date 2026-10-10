package com.example.Backend.crm.dto;


import com.example.Backend.crm.entity.LeadSource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateLeadRequest(

        @NotBlank(message = "Full name is required")
        @Size(max = 100)
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        @Size(max = 254)
        String email,

        @Size(max = 30)
        String phone,

        @Size(max = 150)
        String company,

        @NotNull(message = "Lead source is required")
        LeadSource source,

        @Size(max = 2000)
        String notes

) {
}