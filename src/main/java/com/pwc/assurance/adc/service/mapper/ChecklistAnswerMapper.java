package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.ChecklistAnswerDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ChecklistAnswer and its DTO ChecklistAnswerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChecklistAnswerMapper {

    @Mapping(source = "disclosureRequirement.id", target = "disclosureRequirementId")
    @Mapping(source = "disclosureRequirement.description", target = "disclosureRequirementDescription")
    ChecklistAnswerDTO checklistAnswerToChecklistAnswerDTO(ChecklistAnswer checklistAnswer);

    List<ChecklistAnswerDTO> checklistAnswersToChecklistAnswerDTOs(List<ChecklistAnswer> checklistAnswers);

    @Mapping(source = "disclosureRequirementId", target = "disclosureRequirement")
    @Mapping(target = "checklists", ignore = true)
    ChecklistAnswer checklistAnswerDTOToChecklistAnswer(ChecklistAnswerDTO checklistAnswerDTO);

    List<ChecklistAnswer> checklistAnswerDTOsToChecklistAnswers(List<ChecklistAnswerDTO> checklistAnswerDTOs);

    default DisclosureRequirement disclosureRequirementFromId(Long id) {
        if (id == null) {
            return null;
        }
        DisclosureRequirement disclosureRequirement = new DisclosureRequirement();
        disclosureRequirement.setId(id);
        return disclosureRequirement;
    }
}
