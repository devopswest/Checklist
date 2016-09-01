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

import com.pwc.assurance.adc.domain.enumeration.ChecklistStatus;

/**
 * ChecklistTODO: Workflow review tracking, questions help or guidance fields.Main/core checlists, Supplemental Checklists
 *
 */
@ApiModel(description = ""
    + "ChecklistTODO: Workflow review tracking, questions help or guidance fields.Main/core checlists, Supplemental Checklists"
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

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private String version;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChecklistStatus status;

    @OneToMany(mappedBy = "checklist", fetch=FetchType.EAGER)
    @JsonIgnore

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChecklistQuestion> checklistQuestions = new HashSet<>();

    @OneToMany(mappedBy = "checklist")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AuditProfile> auditProfiles = new HashSet<>();

    @ManyToOne
    private Country country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Checklist name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getVersion() {
        return version;
    }

    public Checklist version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ChecklistStatus getStatus() {
        return status;
    }

    public Checklist status(ChecklistStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ChecklistStatus status) {
        this.status = status;
    }

    public Set<ChecklistQuestion> getChecklistQuestions() {
        return checklistQuestions;
    }

    public Checklist checklistQuestions(Set<ChecklistQuestion> checklistQuestions) {
        this.checklistQuestions = checklistQuestions;
        return this;
    }

    public Checklist addChecklistQuestion(ChecklistQuestion checklistQuestion) {
        checklistQuestions.add(checklistQuestion);
        checklistQuestion.setChecklist(this);
        return this;
    }

    public Checklist removeChecklistQuestion(ChecklistQuestion checklistQuestion) {
        checklistQuestions.remove(checklistQuestion);
        checklistQuestion.setChecklist(null);
        return this;
    }

    public void setChecklistQuestions(Set<ChecklistQuestion> checklistQuestions) {
        this.checklistQuestions = checklistQuestions;
    }

    public Set<AuditProfile> getAuditProfiles() {
        return auditProfiles;
    }

    public Checklist auditProfiles(Set<AuditProfile> auditProfiles) {
        this.auditProfiles = auditProfiles;
        return this;
    }

    public Checklist addAuditProfile(AuditProfile auditProfile) {
        auditProfiles.add(auditProfile);
        auditProfile.setChecklist(this);
        return this;
    }

    public Checklist removeAuditProfile(AuditProfile auditProfile) {
        auditProfiles.remove(auditProfile);
        auditProfile.setChecklist(null);
        return this;
    }

    public void setAuditProfiles(Set<AuditProfile> auditProfiles) {
        this.auditProfiles = auditProfiles;
    }

    public Country getCountry() {
        return country;
    }

    public Checklist country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
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
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", version='" + version + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
