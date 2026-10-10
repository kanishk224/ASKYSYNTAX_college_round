package com.example.Backend.workspace.repository;

import com.example.Backend.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository
        extends JpaRepository<Workspace, Long> {

    List<Workspace> findAllByOwner_EmailOrderByCreatedAtDesc(
            String email
    );

    Optional<Workspace> findByIdAndOwner_Email(
            Long id,
            String email
    );
}
