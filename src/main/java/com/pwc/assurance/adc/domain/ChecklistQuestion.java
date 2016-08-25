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

    @Column(name = "question")
    private String question;

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

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Set<ChecklistQuestion> getChildren() {
        return children;
    }

    public void setChildren(Set<ChecklistQuestion> checklistQuestions) {
        this.children = checklistQuestions;
    }

    public Set<AuditQuestionResponse> getAuditQuestionResponses() {
        return auditQuestionResponses;
    }

    public void setAuditQuestionResponses(Set<AuditQuestionResponse> auditQuestionResponses) {
        this.auditQuestionResponses = auditQuestionResponses;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public ChecklistQuestion getParent() {
        return parent;
    }

    public void setParent(ChecklistQuestion checklistQuestion) {
        this.parent = checklistQuestion;
    }

    public Set<AuditProfile> getAuditProfiles() {
        return auditProfiles;
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
            ", question='" + question + "'" +
            '}';
    }
}
