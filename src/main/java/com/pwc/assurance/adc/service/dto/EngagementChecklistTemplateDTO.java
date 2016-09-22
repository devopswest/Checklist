package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the EngagementChecklistTemplate entity.
 */
public class EngagementChecklistTemplateDTO implements Serializable {

    private Long id;


    private Long checklistTemplateId;
    

    private String checklistTemplateName;

    private Long workflowId;
    

    private String workflowName;

    private Long engagementId;
    

    private String engagementDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }


    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EngagementChecklistTemplateDTO engagementChecklistTemplateDTO = (EngagementChecklistTemplateDTO) o;

        if ( ! Objects.equals(id, engagementChecklistTemplateDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EngagementChecklistTemplateDTO{" +
            "id=" + id +
            '}';
    }
}
