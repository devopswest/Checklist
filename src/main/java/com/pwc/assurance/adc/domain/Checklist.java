package com.pwc.assurance.adc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.ResponseStatus;

/**
 * Response Responses                                                          
 * 
 */
@ApiModel(description = ""
    + "Response Responses                                                     "
    + "")
@Entity
@Table(name = "checklist")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "checklist")
public class Checklist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ResponseStatus status;

    @OneToMany(mappedBy = "checklist")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChecklistHistoryChanges> logs = new HashSet<>();

    @OneToMany(mappedBy = "checklist")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChecklistWorkflow> reviews = new HashSet<>();

    @ManyToOne
    private Engagement engagement;

    @ManyToOne
    private User owner;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "checklist_checklist_answer",
               joinColumns = @JoinColumn(name="checklists_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="checklist_answers_id", referencedColumnName="ID"))
    private Set<ChecklistAnswer> checklistAnswers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Checklist description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public Checklist status(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public Set<ChecklistHistoryChanges> getLogs() {
        return logs;
    }

    public Checklist logs(Set<ChecklistHistoryChanges> checklistHistoryChanges) {
        this.logs = checklistHistoryChanges;
        return this;
    }

    public Checklist addChecklistHistoryChanges(ChecklistHistoryChanges checklistHistoryChanges) {
        logs.add(checklistHistoryChanges);
        checklistHistoryChanges.setChecklist(this);
        return this;
    }

    public Checklist removeChecklistHistoryChanges(ChecklistHistoryChanges checklistHistoryChanges) {
        logs.remove(checklistHistoryChanges);
        checklistHistoryChanges.setChecklist(null);
        return this;
    }

    public void setLogs(Set<ChecklistHistoryChanges> checklistHistoryChanges) {
        this.logs = checklistHistoryChanges;
    }

    public Set<ChecklistWorkflow> getReviews() {
        return reviews;
    }

    public Checklist reviews(Set<ChecklistWorkflow> checklistWorkflows) {
        this.reviews = checklistWorkflows;
        return this;
    }

    public Checklist addChecklistWorkflow(ChecklistWorkflow checklistWorkflow) {
        reviews.add(checklistWorkflow);
        checklistWorkflow.setChecklist(this);
        return this;
    }

    public Checklist removeChecklistWorkflow(ChecklistWorkflow checklistWorkflow) {
        reviews.remove(checklistWorkflow);
        checklistWorkflow.setChecklist(null);
        return this;
    }

    public void setReviews(Set<ChecklistWorkflow> checklistWorkflows) {
        this.reviews = checklistWorkflows;
    }

    public Engagement getEngagement() {
        return engagement;
    }

    public Checklist engagement(Engagement engagement) {
        this.engagement = engagement;
        return this;
    }

    public void setEngagement(Engagement engagement) {
        this.engagement = engagement;
    }

    public User getOwner() {
        return owner;
    }

    public Checklist owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Set<ChecklistAnswer> getChecklistAnswers() {
        return checklistAnswers;
    }

    public Checklist checklistAnswers(Set<ChecklistAnswer> checklistAnswers) {
        this.checklistAnswers = checklistAnswers;
        return this;
    }

    public Checklist addChecklistAnswer(ChecklistAnswer checklistAnswer) {
        checklistAnswers.add(checklistAnswer);
        checklistAnswer.getChecklists().add(this);
        return this;
    }

    public Checklist removeChecklistAnswer(ChecklistAnswer checklistAnswer) {
        checklistAnswers.remove(checklistAnswer);
        checklistAnswer.getChecklists().remove(this);
        return this;
    }

    public void setChecklistAnswers(Set<ChecklistAnswer> checklistAnswers) {
        this.checklistAnswers = checklistAnswers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Checklist checklist = (Checklist) o;
        if(checklist.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, checklist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Checklist{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
