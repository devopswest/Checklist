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

/**
 * LibraryTODO: Questions, Diclosure Requirements, MatrixMatrix vs Client metaada
 * 
 */
@ApiModel(description = ""
    + "LibraryTODO: Questions, Diclosure Requirements, MatrixMatrix vs Client metaada"
    + "")
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "question")
    private String question;

    @OneToMany(mappedBy = "question")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChecklistQuestion> checklistQuestions = new HashSet<>();

    @ManyToMany(mappedBy = "questions")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Requirement> requirements = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Question code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuestion() {
        return question;
    }

    public Question question(String question) {
        this.question = question;
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Set<ChecklistQuestion> getChecklistQuestions() {
        return checklistQuestions;
    }

    public Question checklistQuestions(Set<ChecklistQuestion> checklistQuestions) {
        this.checklistQuestions = checklistQuestions;
        return this;
    }

    public Question addChecklistQuestion(ChecklistQuestion checklistQuestion) {
        checklistQuestions.add(checklistQuestion);
        checklistQuestion.setQuestion(this);
        return this;
    }

    public Question removeChecklistQuestion(ChecklistQuestion checklistQuestion) {
        checklistQuestions.remove(checklistQuestion);
        checklistQuestion.setQuestion(null);
        return this;
    }

    public void setChecklistQuestions(Set<ChecklistQuestion> checklistQuestions) {
        this.checklistQuestions = checklistQuestions;
    }

    public Set<Requirement> getRequirements() {
        return requirements;
    }

    public Question requirements(Set<Requirement> requirements) {
        this.requirements = requirements;
        return this;
    }

    public Question addRequirement(Requirement requirement) {
        requirements.add(requirement);
        requirement.getQuestions().add(this);
        return this;
    }

    public Question removeRequirement(Requirement requirement) {
        requirements.remove(requirement);
        requirement.getQuestions().remove(this);
        return this;
    }

    public void setRequirements(Set<Requirement> requirements) {
        this.requirements = requirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        if(question.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", question='" + question + "'" +
            '}';
    }
}
