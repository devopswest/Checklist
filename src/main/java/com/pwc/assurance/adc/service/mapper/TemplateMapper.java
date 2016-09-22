package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.TemplateDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Template and its DTO TemplateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TemplateMapper {

    TemplateDTO templateToTemplateDTO(Template template);

    List<TemplateDTO> templatesToTemplateDTOs(List<Template> templates);

    Template templateDTOToTemplate(TemplateDTO templateDTO);

    List<Template> templateDTOsToTemplates(List<TemplateDTO> templateDTOs);
}
