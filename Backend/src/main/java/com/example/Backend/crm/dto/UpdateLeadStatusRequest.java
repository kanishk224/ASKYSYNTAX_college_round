package com.example.Backend.crm.dto;


import com.example.Backend.crm.entity.LeadStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateLeadStatusRequest(

        @NotNull(message = "Status is required")
        LeadStatus status

) {
}