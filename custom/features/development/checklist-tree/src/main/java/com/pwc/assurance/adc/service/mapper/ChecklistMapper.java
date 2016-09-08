package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.ChecklistDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Checklist and its DTO ChecklistDTO.
 */
@Mapper(componentModel = "spring", uses = {ChecklistQuestionMapper.class})
public interface ChecklistMapper {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.label", target = "countryLabel")
    ChecklistDTO checklistToChecklistDTO(Checklist checklist);

    List<ChecklistDTO> checklistsToChecklistDTOs(List<Checklist> checklists);

    @Mapping(target = "checklistQuestions", ignore = false)
    @Mapping(source = "countryId", target = "country")
    Checklist checklistDTOToChecklist(ChecklistDTO checklistDTO);

    List<Checklist> checklistDTOsToChecklists(List<ChecklistDTO> checklistDTOs);

    default Taxonomy taxonomyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setId(id);
        return taxonomy;
    }
}
