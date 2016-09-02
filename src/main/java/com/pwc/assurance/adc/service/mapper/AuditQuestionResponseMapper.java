package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.AuditQuestionResponseDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AuditQuestionResponse and its DTO AuditQuestionResponseDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuditQuestionResponseMapper {

    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "question.description", target = "questionDescription")
    AuditQuestionResponseDTO auditQuestionResponseToAuditQuestionResponseDTO(AuditQuestionResponse auditQuestionResponse);

    List<AuditQuestionResponseDTO> auditQuestionResponsesToAuditQuestionResponseDTOs(List<AuditQuestionResponse> auditQuestionResponses);

    @Mapping(source = "questionId", target = "question")
    @Mapping(target = "auditProfiles", ignore = true)
    AuditQuestionResponse auditQuestionResponseDTOToAuditQuestionResponse(AuditQuestionResponseDTO auditQuestionResponseDTO);

    List<AuditQuestionResponse> auditQuestionResponseDTOsToAuditQuestionResponses(List<AuditQuestionResponseDTO> auditQuestionResponseDTOs);

    default ChecklistQuestion checklistQuestionFromId(Long id) {
        if (id == null) {
            return null;
        }
        ChecklistQuestion checklistQuestion = new ChecklistQuestion();
        checklistQuestion.setId(id);
        return checklistQuestion;
    }
}
