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
 * TODO: Answer might be N/A, Yes, No + comments                               
 * 
 */
@ApiModel(description = ""
    + "TODO: Answer might be N/A, Yes, No + comments                          "
    + "")
@Entity
@Table(name = "checklist_answer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "checklistanswer")
public class ChecklistAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "answer")
    private String answer;

    @Column(name = "comments")
    private String comments;

    @ManyToOne
    private DisclosureRequirement disclosureRequirement;

    @ManyToMany(mappedBy = "checklistAnswers")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Checklist> checklists = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public ChecklistAnswer answer(String answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getComments() {
        return comments;
    }

    public ChecklistAnswer comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public DisclosureRequirement getDisclosureRequirement() {
        return disclosureRequirement;
    }

    public ChecklistAnswer disclosureRequirement(DisclosureRequirement disclosureRequirement) {
        this.disclosureRequirement = disclosureRequirement;
        return this;
    }

    public void setDisclosureRequirement(DisclosureRequirement disclosureRequirement) {
        this.disclosureRequirement = disclosureRequirement;
    }

    public Set<Checklist> getChecklists() {
        return checklists;
    }

    public ChecklistAnswer checklists(Set<Checklist> checklists) {
        this.checklists = checklists;
        return this;
    }

    public ChecklistAnswer addChecklist(Checklist checklist) {
        checklists.add(checklist);
        checklist.getChecklistAnswers().add(this);
        return this;
    }

    public ChecklistAnswer removeChecklist(Checklist checklist) {
        checklists.remove(checklist);
        checklist.getChecklistAnswers().remove(this);
        return this;
    }

    public void setChecklists(Set<Checklist> checklists) {
        this.checklists = checklists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChecklistAnswer checklistAnswer = (ChecklistAnswer) o;
        if(checklistAnswer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, checklistAnswer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChecklistAnswer{" +
            "id=" + id +
            ", answer='" + answer + "'" +
            ", comments='" + comments + "'" +
            '}';
    }
}
