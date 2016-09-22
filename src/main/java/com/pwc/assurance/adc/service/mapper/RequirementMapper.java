package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.RequirementDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Requirement and its DTO RequirementDTO.
 */
@Mapper(componentModel = "spring", uses = {DisclosureRequirementMapper.class, })
public interface RequirementMapper {

    RequirementDTO requirementToRequirementDTO(Requirement requirement);

    List<RequirementDTO> requirementsToRequirementDTOs(List<Requirement> requirements);

    Requirement requirementDTOToRequirement(RequirementDTO requirementDTO);

    List<Requirement> requirementDTOsToRequirements(List<RequirementDTO> requirementDTOs);

    default DisclosureRequirement disclosureRequirementFromId(Long id) {
        if (id == null) {
            return null;
        }
        DisclosureRequirement disclosureRequirement = new DisclosureRequirement();
        disclosureRequirement.setId(id);
        return disclosureRequirement;
    }
}
