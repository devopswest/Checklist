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

import com.pwc.assurance.adc.domain.enumeration.ResponseStatus;

/**
 * Response Responses                                                          
 * 
 */
@ApiModel(description = ""
    + "Response Responses                                                     "
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public Set<ChecklistQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<ChecklistQuestion> checklistQuestions) {
        this.questions = checklistQuestions;
    }

    public Set<AuditQuestionResponse> getAuditQuestionResponses() {
        return auditQuestionResponses;
    }

    public void setAuditQuestionResponses(Set<AuditQuestionResponse> auditQuestionResponses) {
        this.auditQuestionResponses = auditQuestionResponses;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
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
