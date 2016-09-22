package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.DisclosureRequirementTagDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity DisclosureRequirementTag and its DTO DisclosureRequirementTagDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DisclosureRequirementTagMapper {

    @Mapping(source = "tagProperty.id", target = "tagPropertyId")
    @Mapping(source = "tagProperty.label", target = "tagPropertyLabel")
    @Mapping(source = "disclosureRequirement.id", target = "disclosureRequirementId")
    @Mapping(source = "disclosureRequirement.description", target = "disclosureRequirementDescription")
    DisclosureRequirementTagDTO disclosureRequirementTagToDisclosureRequirementTagDTO(DisclosureRequirementTag disclosureRequirementTag);

    List<DisclosureRequirementTagDTO> disclosureRequirementTagsToDisclosureRequirementTagDTOs(List<DisclosureRequirementTag> disclosureRequirementTags);

    @Mapping(source = "tagPropertyId", target = "tagProperty")
    @Mapping(source = "disclosureRequirementId", target = "disclosureRequirement")
    DisclosureRequirementTag disclosureRequirementTagDTOToDisclosureRequirementTag(DisclosureRequirementTagDTO disclosureRequirementTagDTO);

    List<DisclosureRequirementTag> disclosureRequirementTagDTOsToDisclosureRequirementTags(List<DisclosureRequirementTagDTO> disclosureRequirementTagDTOs);

    default Taxonomy taxonomyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setId(id);
        return taxonomy;
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
