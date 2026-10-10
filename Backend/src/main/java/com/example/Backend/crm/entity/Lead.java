package com.example.Backend.crm.entity;


import com.example.Backend.workspace.entity.Workspace;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
    @Table(
            name = "leads",
            indexes = {
                    @Index(
                            name = "idx_leads_workspace",
                            columnList = "workspace_id"
                    ),
                    @Index(
                            name = "idx_leads_workspace_status",
                            columnList = "workspace_id, status"
                    )
            }
    )
public class Lead {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 100)
        private String fullName;

        @Column(nullable = false, length = 254)
        private String email;

        @Column(length = 30)
        private String phone;

        @Column(length = 150)
        private String company;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 30)
        private LeadSource source;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 30)
        private LeadStatus status = LeadStatus.NEW;

        @Column(length = 2000)
        private String notes;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "workspace_id", nullable = false)
        private Workspace workspace;

        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(nullable = false)
        private LocalDateTime updatedAt;

    public Lead(String trim, String trim1, String normalize, String normalize1, @NotNull(message = "Lead source is required") LeadSource source, String normalize2, Workspace workspace) {
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDetails(
            String fullName,
            String email,
            String phone,
            String company,
            LeadSource source,
            String notes
    ) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.company = company;
        this.source = source;
        this.notes = notes;
    }

    public void updateStatus(LeadStatus status) {
        this.status = status;
    }
}
