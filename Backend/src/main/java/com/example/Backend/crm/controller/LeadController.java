package com.example.Backend.crm.controller;

import com.example.Backend.crm.dto.CreateLeadRequest;
import com.example.Backend.crm.dto.LeadResponse;
import com.example.Backend.crm.dto.UpdateLeadRequest;
import com.example.Backend.crm.dto.UpdateLeadStatusRequest;
import com.example.Backend.crm.service.LeadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/leads")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public LeadResponse createLead(
            @PathVariable Long workspaceId,
            @Valid @RequestBody CreateLeadRequest request,
            Authentication authentication
    ) {

        return leadService.createLead(
                workspaceId,
                request,
                authentication.getName()
        );
    }

    @GetMapping("/get")
    public List<LeadResponse> getAllLeads(
            @PathVariable Long workspaceId,
            Authentication authentication
    ) {

        return leadService.getAllLeads(
                workspaceId,
                authentication.getName()
        );
    }

    @GetMapping("/{leadId}")
    public LeadResponse getLead(
            @PathVariable Long workspaceId,
            @PathVariable Long leadId,
            Authentication authentication
    ) {

        return leadService.getLead(
                workspaceId,
                leadId,
                authentication.getName()
        );
    }

    @PutMapping("/{leadId}")
    public LeadResponse updateLead(
            @PathVariable Long workspaceId,
            @PathVariable Long leadId,
            @Valid @RequestBody UpdateLeadRequest request,
            Authentication authentication
    ) {

        return leadService.updateLead(
                workspaceId,
                leadId,
                request,
                authentication.getName()
        );
    }

    @PatchMapping("/{leadId}/status")
    public LeadResponse updateLeadStatus(
            @PathVariable Long workspaceId,
            @PathVariable Long leadId,
            @Valid @RequestBody UpdateLeadStatusRequest request,
            Authentication authentication
    ) {

        return leadService.updateLeadStatus(
                workspaceId,
                leadId,
                request,
                authentication.getName()
        );
    }

    @DeleteMapping("/{leadId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLead(
            @PathVariable Long workspaceId,
            @PathVariable Long leadId,
            Authentication authentication
    ) {

        leadService.deleteLead(
                workspaceId,
                leadId,
                authentication.getName()
        );
    }
}