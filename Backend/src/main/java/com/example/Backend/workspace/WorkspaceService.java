package com.example.Backend.workspace;

import com.example.Backend.auth.User;
import com.example.Backend.auth.UserRepository;
import com.example.Backend.workspace.dto.CreateWorkspaceRequest;
import com.example.Backend.workspace.dto.UpdateWorkspaceRequest;
import com.example.Backend.workspace.dto.WorkspaceResponse;
import com.example.Backend.workspace.entity.Workspace;
import com.example.Backend.workspace.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public WorkspaceService(
            WorkspaceRepository workspaceRepository,
            UserRepository userRepository
    ) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public WorkspaceResponse createWorkspace(
            CreateWorkspaceRequest request,
            String authenticatedEmail
    ) {

        User owner = userRepository
                .findByEmail(authenticatedEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Authenticated user not found"
                        )
                );

        Workspace workspace = new Workspace(
                request.name().trim(),
                request.description() == null
                        ? null
                        : request.description().trim(),
                owner
        );

        Workspace savedWorkspace =
                workspaceRepository.save(workspace);

        return WorkspaceResponse.from(savedWorkspace);
    }

    public List<WorkspaceResponse> getMyWorkspaces(
            String authenticatedEmail
    ) {

        return workspaceRepository
                .findAllByOwner_EmailOrderByCreatedAtDesc(
                        authenticatedEmail
                )
                .stream()
                .map(WorkspaceResponse::from)
                .toList();
    }

    public WorkspaceResponse getWorkspace(
            Long workspaceId,
            String authenticatedEmail
    ) {

        Workspace workspace = getOwnedWorkspace(
                workspaceId,
                authenticatedEmail
        );

        return WorkspaceResponse.from(workspace);
    }

    @Transactional
    public WorkspaceResponse updateWorkspace(
            Long workspaceId,
            UpdateWorkspaceRequest request,
            String authenticatedEmail
    ) {

        Workspace workspace = getOwnedWorkspace(
                workspaceId,
                authenticatedEmail
        );

        workspace.update(
                request.name().trim(),
                request.description() == null
                        ? null
                        : request.description().trim()
        );

        Workspace updatedWorkspace =
                workspaceRepository.save(workspace);

        return WorkspaceResponse.from(updatedWorkspace);
    }

    @Transactional
    public void deleteWorkspace(
            Long workspaceId,
            String authenticatedEmail
    ) {

        Workspace workspace = getOwnedWorkspace(
                workspaceId,
                authenticatedEmail
        );

        workspaceRepository.delete(workspace);
    }

    private Workspace getOwnedWorkspace(
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
}
