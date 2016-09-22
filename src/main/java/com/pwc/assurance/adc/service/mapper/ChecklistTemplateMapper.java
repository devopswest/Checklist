package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.ChecklistTemplateDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ChecklistTemplate and its DTO ChecklistTemplateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChecklistTemplateMapper {

    @Mapping(source = "territory.id", target = "territoryId")
    @Mapping(source = "territory.label", target = "territoryLabel")
    @Mapping(source = "formType.id", target = "formTypeId")
    @Mapping(source = "formType.label", target = "formTypeLabel")
    ChecklistTemplateDTO checklistTemplateToChecklistTemplateDTO(ChecklistTemplate checklistTemplate);

    List<ChecklistTemplateDTO> checklistTemplatesToChecklistTemplateDTOs(List<ChecklistTemplate> checklistTemplates);

    @Mapping(target = "disclosureRequirements", ignore = true)
    @Mapping(source = "territoryId", target = "territory")
    @Mapping(source = "formTypeId", target = "formType")
    ChecklistTemplate checklistTemplateDTOToChecklistTemplate(ChecklistTemplateDTO checklistTemplateDTO);

    List<ChecklistTemplate> checklistTemplateDTOsToChecklistTemplates(List<ChecklistTemplateDTO> checklistTemplateDTOs);

    default Taxonomy taxonomyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setId(id);
        return taxonomy;
    }
}
