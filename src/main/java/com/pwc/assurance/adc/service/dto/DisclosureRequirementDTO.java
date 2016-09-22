package com.pwc.assurance.adc.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the DisclosureRequirement entity.
 */
public class DisclosureRequirementDTO implements Serializable {

    private Long id;

    private String code;

    @Size(min = 1, max = 4000)
    private String description;


    private Long checklistTemplateId;
    

    private String checklistTemplateName;

    private Long parentId;
    

    private String parentDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getChecklistTemplateId() {
        return checklistTemplateId;
    }

    public void setChecklistTemplateId(Long checklistTemplateId) {
        this.checklistTemplateId = checklistTemplateId;
    }


    public String getChecklistTemplateName() {
        return checklistTemplateName;
    }

    public void setChecklistTemplateName(String checklistTemplateName) {
        this.checklistTemplateName = checklistTemplateName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long disclosureRequirementId) {
        this.parentId = disclosureRequirementId;
    }


    public String getParentDescription() {
        return parentDescription;
    }

    public void setParentDescription(String disclosureRequirementDescription) {
        this.parentDescription = disclosureRequirementDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DisclosureRequirementDTO disclosureRequirementDTO = (DisclosureRequirementDTO) o;

        if ( ! Objects.equals(id, disclosureRequirementDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DisclosureRequirementDTO{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
