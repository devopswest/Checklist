package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.List;
import java.util.Collections;
import com.pwc.assurance.adc.domain.enumeration.ChecklistStatus;

/**
 * A DTO for the Checklist entity.
 */
public class ChecklistDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String version;

    private ChecklistStatus status;


    private Long countryId;


    private String countryLabel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public ChecklistStatus getStatus() {
        return status;
    }

    public void setStatus(ChecklistStatus status) {
        this.status = status;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long taxonomyId) {
        this.countryId = taxonomyId;
    }


    public String getCountryLabel() {
        return countryLabel;
    }

    public void setCountryLabel(String taxonomyLabel) {
        this.countryLabel = taxonomyLabel;
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
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", version='" + version + "'" +
            ", status='" + status + "'" +
            '}';
    }

    List<ChecklistQuestionDTO> checklistQuestions;

    public List<ChecklistQuestionDTO> getChecklistQuestions() {
        if(checklistQuestions != null){
            Collections.sort(checklistQuestions);
            return checklistQuestions;
        }
        return checklistQuestions;
    }

    public void setChecklistQuestions(List<ChecklistQuestionDTO> checklistQuestions) {
        this.checklistQuestions = checklistQuestions;
    }
}
