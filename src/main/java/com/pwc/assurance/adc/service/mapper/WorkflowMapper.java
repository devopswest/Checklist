package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.WorkflowDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Workflow and its DTO WorkflowDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WorkflowMapper {

    WorkflowDTO workflowToWorkflowDTO(Workflow workflow);

    List<WorkflowDTO> workflowsToWorkflowDTOs(List<Workflow> workflows);

    @Mapping(target = "workflowSteps", ignore = true)
    Workflow workflowDTOToWorkflow(WorkflowDTO workflowDTO);

    List<Workflow> workflowDTOsToWorkflows(List<WorkflowDTO> workflowDTOs);
}
