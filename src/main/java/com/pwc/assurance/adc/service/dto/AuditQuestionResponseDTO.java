package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the AuditQuestionResponse entity.
 */
public class AuditQuestionResponseDTO implements Serializable {

    private Long id;

    private String questionResponse;


    private Long questionId;
    

    private String questionDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getQuestionResponse() {
        return questionResponse;
    }

    public void setQuestionResponse(String questionResponse) {
        this.questionResponse = questionResponse;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long checklistQuestionId) {
        this.questionId = checklistQuestionId;
    }


    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String checklistQuestionDescription) {
        this.questionDescription = checklistQuestionDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuditQuestionResponseDTO auditQuestionResponseDTO = (AuditQuestionResponseDTO) o;
        
        //Included questionId also in equals comparison.
        //AuditProfile stores question responses in java.util.Set and if question Id is not included 
        //then during adding new responses (where id is null) it holds only first response with null 
        if ( !Objects.equals(id, auditQuestionResponseDTO.id) || 
        		!Objects.equals(questionId, auditQuestionResponseDTO.questionId)){
        	return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AuditQuestionResponseDTO{" +
            "id=" + id +
            ", questionResponse='" + questionResponse + "'" +
            ", questionId=" + questionId +
            '}';
    }
}
