package com.example.Backend.crm.repository;

import com.example.Backend.crm.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeadRepository extends JpaRepository<Lead, Long> {

    List<Lead> findAllByWorkspace_IdOrderByCreatedAtDesc(
            Long workspaceId
    );

    Optional<Lead> findByIdAndWorkspace_Id(
            Long leadId,
            Long workspaceId
    );
}