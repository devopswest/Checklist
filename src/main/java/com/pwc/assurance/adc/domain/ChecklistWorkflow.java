package com.pwc.assurance.adc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.WorkflowTaskStatus;

/**
 * A ChecklistWorkflow.
 */
@Entity
@Table(name = "checklist_workflow")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "checklistworkflow")
public class ChecklistWorkflow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "happened")
    private ZonedDateTime happened;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WorkflowTaskStatus status;

    @Column(name = "comments")
    private String comments;

    @ManyToOne
    private Checklist checklist;

    @ManyToOne
    private User who;

    @ManyToOne
    private Workflow workflow;

    @ManyToOne
    private ChecklistAnswer checklistAnswer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getHappened() {
        return happened;
    }

    public ChecklistWorkflow happened(ZonedDateTime happened) {
        this.happened = happened;
        return this;
    }

    public void setHappened(ZonedDateTime happened) {
        this.happened = happened;
    }

    public WorkflowTaskStatus getStatus() {
        return status;
    }

    public ChecklistWorkflow status(WorkflowTaskStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(WorkflowTaskStatus status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public ChecklistWorkflow comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public ChecklistWorkflow checklist(Checklist checklist) {
        this.checklist = checklist;
        return this;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public User getWho() {
        return who;
    }

    public ChecklistWorkflow who(User user) {
        this.who = user;
        return this;
    }

    public void setWho(User user) {
        this.who = user;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public ChecklistWorkflow workflow(Workflow workflow) {
        this.workflow = workflow;
        return this;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public ChecklistAnswer getChecklistAnswer() {
        return checklistAnswer;
    }

    public ChecklistWorkflow checklistAnswer(ChecklistAnswer checklistAnswer) {
        this.checklistAnswer = checklistAnswer;
        return this;
    }

    public void setChecklistAnswer(ChecklistAnswer checklistAnswer) {
        this.checklistAnswer = checklistAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChecklistWorkflow checklistWorkflow = (ChecklistWorkflow) o;
        if(checklistWorkflow.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, checklistWorkflow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChecklistWorkflow{" +
            "id=" + id +
            ", happened='" + happened + "'" +
            ", status='" + status + "'" +
            ", comments='" + comments + "'" +
            '}';
    }
}
