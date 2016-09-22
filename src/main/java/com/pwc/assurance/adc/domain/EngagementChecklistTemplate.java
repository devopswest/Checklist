package com.pwc.assurance.adc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EngagementChecklistTemplate.
 */
@Entity
@Table(name = "engagement_checklist_template")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "engagementchecklisttemplate")
public class EngagementChecklistTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private ChecklistTemplate checklistTemplate;

    @ManyToOne
    private Workflow workflow;

    @ManyToOne
    private Engagement engagement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChecklistTemplate getChecklistTemplate() {
        return checklistTemplate;
    }

    public EngagementChecklistTemplate checklistTemplate(ChecklistTemplate checklistTemplate) {
        this.checklistTemplate = checklistTemplate;
        return this;
    }

    public void setChecklistTemplate(ChecklistTemplate checklistTemplate) {
        this.checklistTemplate = checklistTemplate;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public EngagementChecklistTemplate workflow(Workflow workflow) {
        this.workflow = workflow;
        return this;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public Engagement getEngagement() {
        return engagement;
    }

    public EngagementChecklistTemplate engagement(Engagement engagement) {
        this.engagement = engagement;
        return this;
    }

    public void setEngagement(Engagement engagement) {
        this.engagement = engagement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EngagementChecklistTemplate engagementChecklistTemplate = (EngagementChecklistTemplate) o;
        if(engagementChecklistTemplate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, engagementChecklistTemplate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EngagementChecklistTemplate{" +
            "id=" + id +
            '}';
    }
}
