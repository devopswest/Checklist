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
 * Response ResponsesTODO: Workflow review tracking                            
 * 
 */
@ApiModel(description = ""
    + "Response ResponsesTODO: Workflow review tracking                       "
    + "")
@Entity
@Table(name = "audit_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "auditprofile")
public class AuditProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fiscal_year")
    private String fiscalYear;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ResponseStatus status;

    @OneToMany(mappedBy = "auditProfile")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AuditProfileLogEntry> logs = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "audit_profile_question",
               joinColumns = @JoinColumn(name="audit_profiles_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="questions_id", referencedColumnName="ID"))
    private Set<ChecklistQuestion> questions = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "audit_profile_audit_question_response",
               joinColumns = @JoinColumn(name="audit_profiles_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="audit_question_responses_id", referencedColumnName="ID"))
    private Set<AuditQuestionResponse> auditQuestionResponses = new HashSet<>();

    @ManyToOne
    private Client client;

    @ManyToOne
    private Checklist checklist;

    @ManyToOne
    private Workflow workflow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public AuditProfile fiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
        return this;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getDescription() {
        return description;
    }

    public AuditProfile description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public AuditProfile status(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public Set<AuditProfileLogEntry> getLogs() {
        return logs;
    }

    public AuditProfile logs(Set<AuditProfileLogEntry> auditProfileLogEntries) {
        this.logs = auditProfileLogEntries;
        return this;
    }

    public AuditProfile addAuditProfileLogEntry(AuditProfileLogEntry auditProfileLogEntry) {
        logs.add(auditProfileLogEntry);
        auditProfileLogEntry.setAuditProfile(this);
        return this;
    }

    public AuditProfile removeAuditProfileLogEntry(AuditProfileLogEntry auditProfileLogEntry) {
        logs.remove(auditProfileLogEntry);
        auditProfileLogEntry.setAuditProfile(null);
        return this;
    }

    public void setLogs(Set<AuditProfileLogEntry> auditProfileLogEntries) {
        this.logs = auditProfileLogEntries;
    }

    public Set<ChecklistQuestion> getQuestions() {
        return questions;
    }

    public AuditProfile questions(Set<ChecklistQuestion> checklistQuestions) {
        this.questions = checklistQuestions;
        return this;
    }

    public AuditProfile addChecklistQuestion(ChecklistQuestion checklistQuestion) {
        questions.add(checklistQuestion);
        checklistQuestion.getAuditProfiles().add(this);
        return this;
    }

    public AuditProfile removeChecklistQuestion(ChecklistQuestion checklistQuestion) {
        questions.remove(checklistQuestion);
        checklistQuestion.getAuditProfiles().remove(this);
        return this;
    }

    public void setQuestions(Set<ChecklistQuestion> checklistQuestions) {
        this.questions = checklistQuestions;
    }

    public Set<AuditQuestionResponse> getAuditQuestionResponses() {
        return auditQuestionResponses;
    }

    public AuditProfile auditQuestionResponses(Set<AuditQuestionResponse> auditQuestionResponses) {
        this.auditQuestionResponses = auditQuestionResponses;
        return this;
    }

    public AuditProfile addAuditQuestionResponse(AuditQuestionResponse auditQuestionResponse) {
        auditQuestionResponses.add(auditQuestionResponse);
        auditQuestionResponse.getAuditProfiles().add(this);
        return this;
    }

    public AuditProfile removeAuditQuestionResponse(AuditQuestionResponse auditQuestionResponse) {
        auditQuestionResponses.remove(auditQuestionResponse);
        auditQuestionResponse.getAuditProfiles().remove(this);
        return this;
    }

    public void setAuditQuestionResponses(Set<AuditQuestionResponse> auditQuestionResponses) {
        this.auditQuestionResponses = auditQuestionResponses;
    }

    public Client getClient() {
        return client;
    }

    public AuditProfile client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public AuditProfile checklist(Checklist checklist) {
        this.checklist = checklist;
        return this;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public AuditProfile workflow(Workflow workflow) {
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
        AuditProfile auditProfile = (AuditProfile) o;
        if(auditProfile.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, auditProfile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AuditProfile{" +
            "id=" + id +
            ", fiscalYear='" + fiscalYear + "'" +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
