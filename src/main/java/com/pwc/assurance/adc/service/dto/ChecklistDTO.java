package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.ResponseStatus;

/**
 * A DTO for the Checklist entity.
 */
public class ChecklistDTO implements Serializable {

    private Long id;

    private String description;

    private ResponseStatus status;


    private Long engagementId;
    

    private String engagementDescription;

    private Long ownerId;
    

    private String ownerLogin;

    private Set<ChecklistAnswerDTO> checklistAnswers = new HashSet<>();

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }


    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String userLogin) {
        this.ownerLogin = userLogin;
    }

    public Set<ChecklistAnswerDTO> getChecklistAnswers() {
        return checklistAnswers;
    }

    public void setChecklistAnswers(Set<ChecklistAnswerDTO> checklistAnswers) {
        this.checklistAnswers = checklistAnswers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChecklistDTO checklistDTO = (ChecklistDTO) o;

        if ( ! Objects.equals(id, checklistDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChecklistDTO{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
