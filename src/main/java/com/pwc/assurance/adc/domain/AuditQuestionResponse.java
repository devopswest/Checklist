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
 * relationship ManyToMany {AuditProfile{question(question)} to ChecklistQuestion{auditProfile}}
 * 
 */
@ApiModel(description = ""
    + "relationship ManyToMany {AuditProfile{question(question)} to ChecklistQuestion{auditProfile}}"
    + "")
@Entity
@Table(name = "audit_question_response")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "auditquestionresponse")
public class AuditQuestionResponse implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "question_response")
    private String questionResponse;

    @ManyToOne
    private ChecklistQuestion question;

    @ManyToMany(mappedBy = "auditQuestionResponses", cascade=CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AuditProfile> auditProfiles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionResponse() {
        return questionResponse;
    }

    public AuditQuestionResponse questionResponse(String questionResponse) {
        this.questionResponse = questionResponse;
        return this;
    }

    public void setQuestionResponse(String questionResponse) {
        this.questionResponse = questionResponse;
    }

    public ChecklistQuestion getQuestion() {
        return question;
    }

    public AuditQuestionResponse question(ChecklistQuestion checklistQuestion) {
        this.question = checklistQuestion;
        return this;
    }

    public void setQuestion(ChecklistQuestion checklistQuestion) {
        this.question = checklistQuestion;
    }

    public Set<AuditProfile> getAuditProfiles() {
        return auditProfiles;
    }

    public AuditQuestionResponse auditProfiles(Set<AuditProfile> auditProfiles) {
        this.auditProfiles = auditProfiles;
        return this;
    }

    public AuditQuestionResponse addAuditProfile(AuditProfile auditProfile) {
        auditProfiles.add(auditProfile);
        auditProfile.getAuditQuestionResponses().add(this);
        return this;
    }

    public AuditQuestionResponse removeAuditProfile(AuditProfile auditProfile) {
        auditProfiles.remove(auditProfile);
        auditProfile.getAuditQuestionResponses().remove(this);
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
        AuditQuestionResponse auditQuestionResponse = (AuditQuestionResponse) o;
        if(auditQuestionResponse.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, auditQuestionResponse.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
    	String questionId = "";
    	if(question != null){
    		questionId = ""+ question.getId();
    	}
    	
        return "AuditQuestionResponse{" +
            "id=" + id +
            ", questionResponse='" + questionResponse + "'" +
            ", questionId=" + questionId +
            '}';
    }
    
    public AuditQuestionResponse clone(){
    	AuditQuestionResponse resp = new AuditQuestionResponse();
    	resp.setQuestion(this.getQuestion());
    	resp.setQuestionResponse(this.getQuestionResponse());
    	return resp;
    }
}
