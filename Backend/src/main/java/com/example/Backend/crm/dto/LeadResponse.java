package com.example.Backend.crm.dto;


import com.example.Backend.crm.entity.Lead;
import com.example.Backend.crm.entity.LeadSource;
import com.example.Backend.crm.entity.LeadStatus;

import java.time.LocalDateTime;


public record LeadResponse(

        Long id,
        String fullName,
        String email,
        String phone,
        String company,
        LeadSource source,
        LeadStatus status,
        String notes,
        Long workspaceId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {

    public static LeadResponse from(Lead lead) {

        return new LeadResponse(
                lead.getId(),
                lead.getFullName(),
                lead.getEmail(),
                lead.getPhone(),
                lead.getCompany(),
                lead.getSource(),
                lead.getStatus(),
                lead.getNotes(),
                lead.getWorkspace().getId(),
                lead.getCreatedAt(),
                lead.getUpdatedAt()
        );
    }
}