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
@Table(name = "audit_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "auditprofile")
public class AuditProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ResponseStatus status;

    @OneToMany(mappedBy = "auditProfile")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AuditProfileLogEntry> logs = new HashSet<>();

    @ManyToOne
    private Engagement engagement;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "audit_profile_audit_question_response",
               joinColumns = @JoinColumn(name="audit_profiles_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="audit_question_responses_id", referencedColumnName="ID"))
    private Set<AuditQuestionResponse> auditQuestionResponses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Engagement getEngagement() {
        return engagement;
    }

    public AuditProfile engagement(Engagement engagement) {
        this.engagement = engagement;
        return this;
    }

    public void setEngagement(Engagement engagement) {
        this.engagement = engagement;
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
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
