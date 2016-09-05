package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.ChecklistDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Checklist and its DTO ChecklistDTO.
 */
@Mapper(componentModel = "spring", uses = {ChecklistQuestionMapper.class} )
public interface ChecklistMapper {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.name", target = "countryName")
    ChecklistDTO checklistToChecklistDTO(Checklist checklist);

    List<ChecklistDTO> checklistsToChecklistDTOs(List<Checklist> checklists);

    @Mapping(target = "checklistQuestions", ignore = true)
    @Mapping(target = "auditProfiles", ignore = true)
    @Mapping(source = "countryId", target = "country")
    Checklist checklistDTOToChecklist(ChecklistDTO checklistDTO);

    List<Checklist> checklistDTOsToChecklists(List<ChecklistDTO> checklistDTOs);

    default Country countryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Country country = new Country();
        country.setId(id);
        return country;
    }
}