package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.ApplicationAuthorities;

/**
 * A DTO for the WorkflowStep entity.
 */
public class WorkflowStepDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private ApplicationAuthorities authority;


    private Long templateId;
    

    private String templateDescription;

    private Long workflowId;
    

    private String workflowName;

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
    public ApplicationAuthorities getAuthority() {
        return authority;
    }

    public void setAuthority(ApplicationAuthorities authority) {
        this.authority = authority;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }


    public String getTemplateDescription() {
        return templateDescription;
    }

    public void setTemplateDescription(String templateDescription) {
        this.templateDescription = templateDescription;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkflowStepDTO workflowStepDTO = (WorkflowStepDTO) o;

        if ( ! Objects.equals(id, workflowStepDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WorkflowStepDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", authority='" + authority + "'" +
            '}';
    }
}
