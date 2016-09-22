package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.WorkflowStepDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity WorkflowStep and its DTO WorkflowStepDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WorkflowStepMapper {

    @Mapping(source = "template.id", target = "templateId")
    @Mapping(source = "template.description", target = "templateDescription")
    @Mapping(source = "workflow.id", target = "workflowId")
    @Mapping(source = "workflow.name", target = "workflowName")
    WorkflowStepDTO workflowStepToWorkflowStepDTO(WorkflowStep workflowStep);

    List<WorkflowStepDTO> workflowStepsToWorkflowStepDTOs(List<WorkflowStep> workflowSteps);

    @Mapping(source = "templateId", target = "template")
    @Mapping(source = "workflowId", target = "workflow")
    WorkflowStep workflowStepDTOToWorkflowStep(WorkflowStepDTO workflowStepDTO);

    List<WorkflowStep> workflowStepDTOsToWorkflowSteps(List<WorkflowStepDTO> workflowStepDTOs);

    default Template templateFromId(Long id) {
        if (id == null) {
            return null;
        }
        Template template = new Template();
        template.setId(id);
        return template;
    }

    default Workflow workflowFromId(Long id) {
        if (id == null) {
            return null;
        }
        Workflow workflow = new Workflow();
        workflow.setId(id);
        return workflow;
    }
}
