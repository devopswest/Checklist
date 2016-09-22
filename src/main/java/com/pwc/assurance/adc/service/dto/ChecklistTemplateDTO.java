package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.ChecklistTemplateStatus;

/**
 * A DTO for the ChecklistTemplate entity.
 */
public class ChecklistTemplateDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String version;

    private ChecklistTemplateStatus status;


    private Long territoryId;
    

    private String territoryLabel;

    private Long formTypeId;
    

    private String formTypeLabel;

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
    public ChecklistTemplateStatus getStatus() {
        return status;
    }

    public void setStatus(ChecklistTemplateStatus status) {
        this.status = status;
    }

    public Long getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(Long taxonomyId) {
        this.territoryId = taxonomyId;
    }


    public String getTerritoryLabel() {
        return territoryLabel;
    }

    public void setTerritoryLabel(String taxonomyLabel) {
        this.territoryLabel = taxonomyLabel;
    }

    public Long getFormTypeId() {
        return formTypeId;
    }

    public void setFormTypeId(Long taxonomyId) {
        this.formTypeId = taxonomyId;
    }


    public String getFormTypeLabel() {
        return formTypeLabel;
    }

    public void setFormTypeLabel(String taxonomyLabel) {
        this.formTypeLabel = taxonomyLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChecklistTemplateDTO checklistTemplateDTO = (ChecklistTemplateDTO) o;

        if ( ! Objects.equals(id, checklistTemplateDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChecklistTemplateDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", version='" + version + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
