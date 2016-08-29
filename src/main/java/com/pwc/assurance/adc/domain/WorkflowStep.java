package com.pwc.assurance.adc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.WorkflowAuthorities;

/**
 * A WorkflowStep.
 */
@Entity
@Table(name = "workflow_step")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "workflowstep")
public class WorkflowStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private WorkflowAuthorities authority;

    @ManyToMany(mappedBy = "workflowSteps")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Workflow> workflows = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public WorkflowStep name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public WorkflowStep description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WorkflowAuthorities getAuthority() {
        return authority;
    }

    public WorkflowStep authority(WorkflowAuthorities authority) {
        this.authority = authority;
        return this;
    }

    public void setAuthority(WorkflowAuthorities authority) {
        this.authority = authority;
    }

    public Set<Workflow> getWorkflows() {
        return workflows;
    }

    public WorkflowStep workflows(Set<Workflow> workflows) {
        this.workflows = workflows;
        return this;
    }

    public WorkflowStep addWorkflow(Workflow workflow) {
        workflows.add(workflow);
        workflow.getWorkflowSteps().add(this);
        return this;
    }

    public WorkflowStep removeWorkflow(Workflow workflow) {
        workflows.remove(workflow);
        workflow.getWorkflowSteps().remove(this);
        return this;
    }

    public void setWorkflows(Set<Workflow> workflows) {
        this.workflows = workflows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkflowStep workflowStep = (WorkflowStep) o;
        if(workflowStep.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, workflowStep.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WorkflowStep{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", authority='" + authority + "'" +
            '}';
    }
}
