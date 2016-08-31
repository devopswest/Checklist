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

/**
 * A ChecklistQuestion.
 */
@Entity
@Table(name = "checklist_question")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "checklistquestion")
public class ChecklistQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChecklistQuestion> children = new HashSet<>();

    @OneToMany(mappedBy = "question")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AuditQuestionResponse> auditQuestionResponses = new HashSet<>();

    @ManyToOne
    private Checklist checklist;

    @ManyToOne
    private ChecklistQuestion parent;

    @ManyToOne
    private Question question;

    @ManyToMany(mappedBy = "questions")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AuditProfile> auditProfiles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public ChecklistQuestion code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public ChecklistQuestion description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ChecklistQuestion> getChildren() {
        return children;
    }

    public ChecklistQuestion children(Set<ChecklistQuestion> checklistQuestions) {
        this.children = checklistQuestions;
        return this;
    }

    public ChecklistQuestion addChecklistQuestion(ChecklistQuestion checklistQuestion) {
        children.add(checklistQuestion);
        checklistQuestion.setParent(this);
        return this;
    }

    public ChecklistQuestion removeChecklistQuestion(ChecklistQuestion checklistQuestion) {
        children.remove(checklistQuestion);
        checklistQuestion.setParent(null);
        return this;
    }

    public void setChildren(Set<ChecklistQuestion> checklistQuestions) {
        this.children = checklistQuestions;
    }

    public Set<AuditQuestionResponse> getAuditQuestionResponses() {
        return auditQuestionResponses;
    }

    public ChecklistQuestion auditQuestionResponses(Set<AuditQuestionResponse> auditQuestionResponses) {
        this.auditQuestionResponses = auditQuestionResponses;
        return this;
    }

    public ChecklistQuestion addAuditQuestionResponse(AuditQuestionResponse auditQuestionResponse) {
        auditQuestionResponses.add(auditQuestionResponse);
        auditQuestionResponse.setQuestion(this);
        return this;
    }

    public ChecklistQuestion removeAuditQuestionResponse(AuditQuestionResponse auditQuestionResponse) {
        auditQuestionResponses.remove(auditQuestionResponse);
        auditQuestionResponse.setQuestion(null);
        return this;
    }

    public void setAuditQuestionResponses(Set<AuditQuestionResponse> auditQuestionResponses) {
        this.auditQuestionResponses = auditQuestionResponses;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public ChecklistQuestion checklist(Checklist checklist) {
        this.checklist = checklist;
        return this;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public ChecklistQuestion getParent() {
        return parent;
    }

    public ChecklistQuestion parent(ChecklistQuestion checklistQuestion) {
        this.parent = checklistQuestion;
        return this;
    }

    public void setParent(ChecklistQuestion checklistQuestion) {
        this.parent = checklistQuestion;
    }

    public Question getQuestion() {
        return question;
    }

    public ChecklistQuestion question(Question question) {
        this.question = question;
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Set<AuditProfile> getAuditProfiles() {
        return auditProfiles;
    }

    public ChecklistQuestion auditProfiles(Set<AuditProfile> auditProfiles) {
        this.auditProfiles = auditProfiles;
        return this;
    }

    public ChecklistQuestion addAuditProfile(AuditProfile auditProfile) {
        auditProfiles.add(auditProfile);
        auditProfile.getQuestions().add(this);
        return this;
    }

    public ChecklistQuestion removeAuditProfile(AuditProfile auditProfile) {
        auditProfiles.remove(auditProfile);
        auditProfile.getQuestions().remove(this);
        return this;
    }

    public void setAuditProfiles(Set<AuditProfile> auditProfiles) {
        this.auditProfiles = auditProfiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChecklistQuestion checklistQuestion = (ChecklistQuestion) o;
        if(checklistQuestion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, checklistQuestion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChecklistQuestion{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
