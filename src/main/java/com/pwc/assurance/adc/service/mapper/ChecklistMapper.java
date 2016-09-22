package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.ChecklistDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Checklist and its DTO ChecklistDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ChecklistAnswerMapper.class, })
public interface ChecklistMapper {

    @Mapping(source = "engagement.id", target = "engagementId")
    @Mapping(source = "engagement.description", target = "engagementDescription")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.login", target = "ownerLogin")
    ChecklistDTO checklistToChecklistDTO(Checklist checklist);

    List<ChecklistDTO> checklistsToChecklistDTOs(List<Checklist> checklists);

    @Mapping(target = "logs", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(source = "engagementId", target = "engagement")
    @Mapping(source = "ownerId", target = "owner")
    Checklist checklistDTOToChecklist(ChecklistDTO checklistDTO);

    List<Checklist> checklistDTOsToChecklists(List<ChecklistDTO> checklistDTOs);

    default Engagement engagementFromId(Long id) {
        if (id == null) {
            return null;
        }
        Engagement engagement = new Engagement();
        engagement.setId(id);
        return engagement;
    }

    default ChecklistAnswer checklistAnswerFromId(Long id) {
        if (id == null) {
            return null;
        }
        ChecklistAnswer checklistAnswer = new ChecklistAnswer();
        checklistAnswer.setId(id);
        return checklistAnswer;
    }
}
