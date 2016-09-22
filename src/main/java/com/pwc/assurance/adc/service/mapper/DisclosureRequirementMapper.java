package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.DisclosureRequirementDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity DisclosureRequirement and its DTO DisclosureRequirementDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DisclosureRequirementMapper {

    @Mapping(source = "checklistTemplate.id", target = "checklistTemplateId")
    @Mapping(source = "checklistTemplate.name", target = "checklistTemplateName")
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.description", target = "parentDescription")
    DisclosureRequirementDTO disclosureRequirementToDisclosureRequirementDTO(DisclosureRequirement disclosureRequirement);

    List<DisclosureRequirementDTO> disclosureRequirementsToDisclosureRequirementDTOs(List<DisclosureRequirement> disclosureRequirements);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(source = "checklistTemplateId", target = "checklistTemplate")
    @Mapping(source = "parentId", target = "parent")
    @Mapping(target = "requirements", ignore = true)
    DisclosureRequirement disclosureRequirementDTOToDisclosureRequirement(DisclosureRequirementDTO disclosureRequirementDTO);

    List<DisclosureRequirement> disclosureRequirementDTOsToDisclosureRequirements(List<DisclosureRequirementDTO> disclosureRequirementDTOs);

    default ChecklistTemplate checklistTemplateFromId(Long id) {
        if (id == null) {
            return null;
        }
        ChecklistTemplate checklistTemplate = new ChecklistTemplate();
        checklistTemplate.setId(id);
        return checklistTemplate;
    }

    default DisclosureRequirement disclosureRequirementFromId(Long id) {
        if (id == null) {
            return null;
        }
        DisclosureRequirement disclosureRequirement = new DisclosureRequirement();
        disclosureRequirement.setId(id);
        return disclosureRequirement;
    }
}
