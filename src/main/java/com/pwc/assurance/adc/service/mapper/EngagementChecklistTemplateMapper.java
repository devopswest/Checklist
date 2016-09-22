package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.EngagementChecklistTemplateDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity EngagementChecklistTemplate and its DTO EngagementChecklistTemplateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EngagementChecklistTemplateMapper {

    @Mapping(source = "checklistTemplate.id", target = "checklistTemplateId")
    @Mapping(source = "checklistTemplate.name", target = "checklistTemplateName")
    @Mapping(source = "workflow.id", target = "workflowId")
    @Mapping(source = "workflow.name", target = "workflowName")
    @Mapping(source = "engagement.id", target = "engagementId")
    @Mapping(source = "engagement.description", target = "engagementDescription")
    EngagementChecklistTemplateDTO engagementChecklistTemplateToEngagementChecklistTemplateDTO(EngagementChecklistTemplate engagementChecklistTemplate);

    List<EngagementChecklistTemplateDTO> engagementChecklistTemplatesToEngagementChecklistTemplateDTOs(List<EngagementChecklistTemplate> engagementChecklistTemplates);

    @Mapping(source = "checklistTemplateId", target = "checklistTemplate")
    @Mapping(source = "workflowId", target = "workflow")
    @Mapping(source = "engagementId", target = "engagement")
    EngagementChecklistTemplate engagementChecklistTemplateDTOToEngagementChecklistTemplate(EngagementChecklistTemplateDTO engagementChecklistTemplateDTO);

    List<EngagementChecklistTemplate> engagementChecklistTemplateDTOsToEngagementChecklistTemplates(List<EngagementChecklistTemplateDTO> engagementChecklistTemplateDTOs);

    default ChecklistTemplate checklistTemplateFromId(Long id) {
        if (id == null) {
            return null;
        }
        ChecklistTemplate checklistTemplate = new ChecklistTemplate();
        checklistTemplate.setId(id);
        return checklistTemplate;
    }

    default Workflow workflowFromId(Long id) {
        if (id == null) {
            return null;
        }
        Workflow workflow = new Workflow();
        workflow.setId(id);
        return workflow;
    }

    default Engagement engagementFromId(Long id) {
        if (id == null) {
            return null;
        }
        Engagement engagement = new Engagement();
        engagement.setId(id);
        return engagement;
    }
}
