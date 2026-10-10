package com.example.Backend.crm.dto;

import com.example.Backend.crm.entity.LeadSource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateLeadRequest(

        @NotBlank(message = "Full name is required")
        @Size(max = 100, message = "Full name cannot exceed 100 characters")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        @Size(max = 254, message = "Email cannot exceed 254 characters")
        String email,

        @Size(max = 30, message = "Phone cannot exceed 30 characters")
        String phone,

        @Size(max = 150, message = "Company cannot exceed 150 characters")
        String company,

        @NotNull(message = "Lead source is required")
        LeadSource source,

        @Size(max = 2000, message = "Notes cannot exceed 2000 characters")
        String notes

) {
}