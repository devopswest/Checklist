package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.FeatureDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Feature and its DTO FeatureDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FeatureMapper {

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.label", target = "parentLabel")
    FeatureDTO featureToFeatureDTO(Feature feature);

    List<FeatureDTO> featuresToFeatureDTOs(List<Feature> features);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(source = "parentId", target = "parent")
    Feature featureDTOToFeature(FeatureDTO featureDTO);

    List<Feature> featureDTOsToFeatures(List<FeatureDTO> featureDTOs);

    default Feature featureFromId(Long id) {
        if (id == null) {
            return null;
        }
        Feature feature = new Feature();
        feature.setId(id);
        return feature;
    }
}
