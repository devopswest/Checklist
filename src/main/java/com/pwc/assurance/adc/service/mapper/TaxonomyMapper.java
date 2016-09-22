package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.TaxonomyDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Taxonomy and its DTO TaxonomyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaxonomyMapper {

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.label", target = "parentLabel")
    TaxonomyDTO taxonomyToTaxonomyDTO(Taxonomy taxonomy);

    List<TaxonomyDTO> taxonomiesToTaxonomyDTOs(List<Taxonomy> taxonomies);

    @Mapping(target = "children", ignore = true)
    @Mapping(source = "parentId", target = "parent")
    Taxonomy taxonomyDTOToTaxonomy(TaxonomyDTO taxonomyDTO);

    List<Taxonomy> taxonomyDTOsToTaxonomies(List<TaxonomyDTO> taxonomyDTOs);

    default Taxonomy taxonomyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setId(id);
        return taxonomy;
    }
}
