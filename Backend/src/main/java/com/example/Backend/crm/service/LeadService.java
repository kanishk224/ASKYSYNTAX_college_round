package com.example.Backend.crm.service;

import com.example.Backend.auth.User;
import com.example.Backend.auth.UserRepository;

import com.example.Backend.crm.dto.CreateLeadRequest;
import com.example.Backend.crm.dto.LeadResponse;
import com.example.Backend.crm.dto.UpdateLeadRequest;

import com.example.Backend.crm.dto.UpdateLeadStatusRequest;
import com.example.Backend.crm.entity.Lead;
import com.example.Backend.crm.repository.LeadRepository;

import com.example.Backend.workspace.entity.Workspace;
import com.example.Backend.workspace.repository.WorkspaceRepository;

import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LeadService {

    private final LeadRepository leadRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public LeadService(
            LeadRepository leadRepository,
            WorkspaceRepository workspaceRepository,
            UserRepository userRepository
    ) {
        this.leadRepository = leadRepository;
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
    }

    // CREATE LEAD
    @Transactional
    public LeadResponse createLead(
            Long workspaceId,
            CreateLeadRequest request,
            String authenticatedEmail
    ) {

        User owner = findAuthenticatedUser(authenticatedEmail);

        Workspace workspace = findOwnedWorkspace(
                workspaceId,
                owner.getEmail()
        );

        Lead lead = new Lead(
                request.fullName().trim(),
                request.email().trim(),
                normalize(request.phone()),
                normalize(request.company()),
                request.source(),
                normalize(request.notes()),
                workspace
        );

        Lead savedLead = leadRepository.save(lead);

        return LeadResponse.from(savedLead);
    }

    public List<LeadResponse> getAllLeads(
            Long workspaceId,
            String authenticatedEmail
    ) {

        Workspace workspace = findOwnedWorkspace(
                workspaceId,
                authenticatedEmail
        );

        return leadRepository
                .findAllByWorkspace_IdOrderByCreatedAtDesc(
                        workspace.getId()
                )
                .stream()
                .map(LeadResponse::from)
                .toList();
    }

    public LeadResponse getLead(
            Long workspaceId,
            Long leadId,
            String authenticatedEmail
    ) {

        Workspace workspace = findOwnedWorkspace(
                workspaceId,
                authenticatedEmail
        );

        Lead lead = findLeadInWorkspace(
                leadId,
                workspace.getId()
        );

        return LeadResponse.from(lead);
    }

    public LeadResponse updateLead(
            Long workspaceId,
            Long leadId,
            UpdateLeadRequest request,
            String authenticatedEmail
    ) {

        Workspace workspace = findOwnedWorkspace(
                workspaceId,
                authenticatedEmail
        );

        Lead lead = findLeadInWorkspace(
                leadId,
                workspace.getId()
        );

        lead.updateDetails(
                request.fullName().trim(),
                request.email().trim(),
                normalize(request.phone()),
                normalize(request.company()),
                request.source(),
                normalize(request.notes())
        );

        Lead updatedLead = leadRepository.save(lead);

        return LeadResponse.from(updatedLead);
    }

    // UPDATE LEAD STATUS
    @Transactional
    public LeadResponse updateLeadStatus(
            Long workspaceId,
            Long leadId,
            UpdateLeadStatusRequest request,
            String authenticatedEmail
    ) {

        Workspace workspace = findOwnedWorkspace(
                workspaceId,
                authenticatedEmail
        );

        Lead lead = findLeadInWorkspace(
                leadId,
                workspace.getId()
        );

        lead.updateStatus(request.status());

        Lead updatedLead = leadRepository.save(lead);

        return LeadResponse.from(updatedLead);
    }

    // DELETE LEAD
    @Transactional
    public void deleteLead(
            Long workspaceId,
            Long leadId,
            String authenticatedEmail
    ) {

        Workspace workspace = findOwnedWorkspace(
                workspaceId,
                authenticatedEmail
        );

        Lead lead = findLeadInWorkspace(
                leadId,
                workspace.getId()
        );

        leadRepository.delete(lead);
    }

    // FIND AUTHENTICATED USER
    private User findAuthenticatedUser(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Authenticated user not found"
                        )
                );
    }

    // FIND WORKSPACE OWNED BY THE AUTHENTICATED USER
    private Workspace findOwnedWorkspace(
            Long workspaceId,
            String authenticatedEmail
    ) {

        return workspaceRepository
                .findByIdAndOwner_Email(
                        workspaceId,
                        authenticatedEmail
                )
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Workspace not found"
                        )
                );
    }

    // FIND LEAD INSIDE THE AUTHORIZED WORKSPACE
    private Lead findLeadInWorkspace(
            Long leadId,
            Long workspaceId
    ) {

        return leadRepository
                .findByIdAndWorkspace_Id(
                        leadId,
                        workspaceId
                )
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Lead not found"
                        )
                );
    }

    // NORMALIZE OPTIONAL STRINGS
    private String normalize(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

}