package com.example.Backend.workspace.dto;


import com.example.Backend.workspace.entity.Workspace;

import java.time.LocalDateTime;

public record WorkspaceResponse(

        Long id,
        String name,
        String description,
        Long ownerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {

    public static WorkspaceResponse from(Workspace workspace) {

        return new WorkspaceResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription(),
                workspace.getOwner().getId(),
                workspace.getCreatedAt(),
                workspace.getUpdatedAt()
        );
    }
}