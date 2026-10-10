package com.example.Backend.workspace.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateWorkspaceRequest(

        @NotBlank(message = "Workspace name is required")
        @Size(max = 100, message = "Workspace name cannot exceed 100 characters")
        String name,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description

) {
}