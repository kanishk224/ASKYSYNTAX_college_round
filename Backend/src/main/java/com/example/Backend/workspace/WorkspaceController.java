package com.example.Backend.workspace;


import com.example.Backend.workspace.dto.CreateWorkspaceRequest;
import com.example.Backend.workspace.dto.UpdateWorkspaceRequest;
import com.example.Backend.workspace.dto.WorkspaceResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(
            WorkspaceService workspaceService
    ) {
        this.workspaceService = workspaceService;
    }

    @PostMapping("/create")
    public ResponseEntity<WorkspaceResponse> createWorkspace(
            @Valid @RequestBody CreateWorkspaceRequest request,
            Authentication authentication
    ) {

        WorkspaceResponse response =
                workspaceService.createWorkspace(
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/getSpaces")
    public ResponseEntity<List<WorkspaceResponse>> getMyWorkspaces(
            Authentication authentication
    ) {

        List<WorkspaceResponse> workspaces =
                workspaceService.getMyWorkspaces(
                        authentication.getName()
                );

        return ResponseEntity.ok(workspaces);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceResponse> getWorkspace(
            @PathVariable Long id,
            Authentication authentication
    ) {

        WorkspaceResponse response =
                workspaceService.getWorkspace(
                        id,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkspaceResponse> updateWorkspace(
            @PathVariable Long id,
            @Valid @RequestBody UpdateWorkspaceRequest request,
            Authentication authentication
    ) {

        WorkspaceResponse response =
                workspaceService.updateWorkspace(
                        id,
                        request,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(
            @PathVariable Long id,
            Authentication authentication
    ) {

        workspaceService.deleteWorkspace(
                id,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }

}
