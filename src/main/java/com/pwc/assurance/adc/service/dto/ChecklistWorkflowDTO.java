package com.pwc.assurance.adc.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.WorkflowTaskStatus;

/**
 * A DTO for the ChecklistWorkflow entity.
 */
public class ChecklistWorkflowDTO implements Serializable {

    private Long id;

    private ZonedDateTime happened;

    private WorkflowTaskStatus status;

    private String comments;


    private Long checklistId;
    
    private Long whoId;
    

    private String whoLogin;

    private Long workflowId;
    

    private String workflowName;

    private Long checklistAnswerId;
    

    private String checklistAnswerAnswer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getHappened() {
        return happened;
    }

    public void setHappened(ZonedDateTime happened) {
        this.happened = happened;
    }
    public WorkflowTaskStatus getStatus() {
        return status;
    }

    public void setStatus(WorkflowTaskStatus status) {
        this.status = status;
    }
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }

    public Long getWhoId() {
        return whoId;
    }

    public void setWhoId(Long userId) {
        this.whoId = userId;
    }


    public String getWhoLogin() {
        return whoLogin;
    }

    public void setWhoLogin(String userLogin) {
        this.whoLogin = userLogin;
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

    public Long getChecklistAnswerId() {
        return checklistAnswerId;
    }

    public void setChecklistAnswerId(Long checklistAnswerId) {
        this.checklistAnswerId = checklistAnswerId;
    }


    public String getChecklistAnswerAnswer() {
        return checklistAnswerAnswer;
    }

    public void setChecklistAnswerAnswer(String checklistAnswerAnswer) {
        this.checklistAnswerAnswer = checklistAnswerAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChecklistWorkflowDTO checklistWorkflowDTO = (ChecklistWorkflowDTO) o;

        if ( ! Objects.equals(id, checklistWorkflowDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChecklistWorkflowDTO{" +
            "id=" + id +
            ", happened='" + happened + "'" +
            ", status='" + status + "'" +
            ", comments='" + comments + "'" +
            '}';
    }
}
