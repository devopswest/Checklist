package com.pwc.assurance.adc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.ApplicationAuthorities;

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
    private ApplicationAuthorities authority;

    @ManyToOne
    private Template template;

    @ManyToOne
    private Workflow workflow;

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

    public ApplicationAuthorities getAuthority() {
        return authority;
    }

    public WorkflowStep authority(ApplicationAuthorities authority) {
        this.authority = authority;
        return this;
    }

    public void setAuthority(ApplicationAuthorities authority) {
        this.authority = authority;
    }

    public Template getTemplate() {
        return template;
    }

    public WorkflowStep template(Template template) {
        this.template = template;
        return this;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public WorkflowStep workflow(Workflow workflow) {
        this.workflow = workflow;
        return this;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
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
