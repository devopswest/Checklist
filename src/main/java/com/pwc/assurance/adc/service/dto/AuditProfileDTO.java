package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.ResponseStatus;

/**
 * A DTO for the AuditProfile entity.
 */
public class AuditProfileDTO implements Serializable {

    private Long id;

    private String description;

    private ResponseStatus status;


    private Set<AuditQuestionResponseDTO> auditQuestionResponses = new HashSet<>();

    private Long engagementId;
    

    private String engagementDescription;

    private Long checklistId;
    

    private String checklistName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public Set<AuditQuestionResponseDTO> getAuditQuestionResponses() {
        return auditQuestionResponses;
    }

    public void setAuditQuestionResponses(Set<AuditQuestionResponseDTO> auditQuestionResponses) {
        this.auditQuestionResponses = auditQuestionResponses;
    }

    public Long getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(Long engagementId) {
        this.engagementId = engagementId;
    }


    public String getEngagementDescription() {
        return engagementDescription;
    }

    public void setEngagementDescription(String engagementDescription) {
        this.engagementDescription = engagementDescription;
    }

    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }


    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuditProfileDTO auditProfileDTO = (AuditProfileDTO) o;

        if ( ! Objects.equals(id, auditProfileDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AuditProfileDTO{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
