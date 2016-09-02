package com.pwc.assurance.adc.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * WorkflowTODO: Workflow review tracking                                      
 * 
 */
@ApiModel(description = ""
    + "WorkflowTODO: Workflow review tracking                                 "
    + "")
@Entity
@Table(name = "workflow")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "workflow")
public class Workflow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "workflow_workflow_step",
               joinColumns = @JoinColumn(name="workflows_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="workflow_steps_id", referencedColumnName="ID"))
    private Set<WorkflowStep> workflowSteps = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Workflow name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Workflow description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<WorkflowStep> getWorkflowSteps() {
        return workflowSteps;
    }

    public Workflow workflowSteps(Set<WorkflowStep> workflowSteps) {
        this.workflowSteps = workflowSteps;
        return this;
    }

    public Workflow addWorkflowStep(WorkflowStep workflowStep) {
        workflowSteps.add(workflowStep);
        workflowStep.getWorkflows().add(this);
        return this;
    }

    public Workflow removeWorkflowStep(WorkflowStep workflowStep) {
        workflowSteps.remove(workflowStep);
        workflowStep.getWorkflows().remove(this);
        return this;
    }

    public void setWorkflowSteps(Set<WorkflowStep> workflowSteps) {
        this.workflowSteps = workflowSteps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Workflow workflow = (Workflow) o;
        if(workflow.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, workflow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Workflow{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
