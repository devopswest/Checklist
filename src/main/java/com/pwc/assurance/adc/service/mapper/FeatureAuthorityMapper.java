package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.FeatureAuthorityDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity FeatureAuthority and its DTO FeatureAuthorityDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FeatureAuthorityMapper {

    @Mapping(source = "feature.id", target = "featureId")
    @Mapping(source = "feature.label", target = "featureLabel")
    FeatureAuthorityDTO featureAuthorityToFeatureAuthorityDTO(FeatureAuthority featureAuthority);

    List<FeatureAuthorityDTO> featureAuthoritiesToFeatureAuthorityDTOs(List<FeatureAuthority> featureAuthorities);

    @Mapping(source = "featureId", target = "feature")
    FeatureAuthority featureAuthorityDTOToFeatureAuthority(FeatureAuthorityDTO featureAuthorityDTO);

    List<FeatureAuthority> featureAuthorityDTOsToFeatureAuthorities(List<FeatureAuthorityDTO> featureAuthorityDTOs);

    default Feature featureFromId(Long id) {
        if (id == null) {
            return null;
        }
        Feature feature = new Feature();
        feature.setId(id);
        return feature;
    }
}
