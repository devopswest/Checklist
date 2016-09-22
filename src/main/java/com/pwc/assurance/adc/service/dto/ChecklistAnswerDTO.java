package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ChecklistAnswer entity.
 */
public class ChecklistAnswerDTO implements Serializable {

    private Long id;

    private String answer;

    private String comments;


    private Long disclosureRequirementId;
    

    private String disclosureRequirementDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getDisclosureRequirementId() {
        return disclosureRequirementId;
    }

    public void setDisclosureRequirementId(Long disclosureRequirementId) {
        this.disclosureRequirementId = disclosureRequirementId;
    }


    public String getDisclosureRequirementDescription() {
        return disclosureRequirementDescription;
    }

    public void setDisclosureRequirementDescription(String disclosureRequirementDescription) {
        this.disclosureRequirementDescription = disclosureRequirementDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChecklistAnswerDTO checklistAnswerDTO = (ChecklistAnswerDTO) o;

        if ( ! Objects.equals(id, checklistAnswerDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChecklistAnswerDTO{" +
            "id=" + id +
            ", answer='" + answer + "'" +
            ", comments='" + comments + "'" +
            '}';
    }
}
