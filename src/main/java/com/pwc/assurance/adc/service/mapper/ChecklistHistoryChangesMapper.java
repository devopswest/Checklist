package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.ChecklistHistoryChangesDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ChecklistHistoryChanges and its DTO ChecklistHistoryChangesDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface ChecklistHistoryChangesMapper {

    @Mapping(source = "checklist.id", target = "checklistId")
    @Mapping(source = "who.id", target = "whoId")
    @Mapping(source = "who.login", target = "whoLogin")
    ChecklistHistoryChangesDTO checklistHistoryChangesToChecklistHistoryChangesDTO(ChecklistHistoryChanges checklistHistoryChanges);

    List<ChecklistHistoryChangesDTO> checklistHistoryChangesToChecklistHistoryChangesDTOs(List<ChecklistHistoryChanges> checklistHistoryChanges);

    @Mapping(source = "checklistId", target = "checklist")
    @Mapping(source = "whoId", target = "who")
    ChecklistHistoryChanges checklistHistoryChangesDTOToChecklistHistoryChanges(ChecklistHistoryChangesDTO checklistHistoryChangesDTO);

    List<ChecklistHistoryChanges> checklistHistoryChangesDTOsToChecklistHistoryChanges(List<ChecklistHistoryChangesDTO> checklistHistoryChangesDTOs);

    default Checklist checklistFromId(Long id) {
        if (id == null) {
            return null;
        }
        Checklist checklist = new Checklist();
        checklist.setId(id);
        return checklist;
    }
}
