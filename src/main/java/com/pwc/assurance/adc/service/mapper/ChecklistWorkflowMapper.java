package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.ChecklistWorkflowDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ChecklistWorkflow and its DTO ChecklistWorkflowDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface ChecklistWorkflowMapper {

    @Mapping(source = "checklist.id", target = "checklistId")
    @Mapping(source = "who.id", target = "whoId")
    @Mapping(source = "who.login", target = "whoLogin")
    @Mapping(source = "workflow.id", target = "workflowId")
    @Mapping(source = "workflow.name", target = "workflowName")
    @Mapping(source = "checklistAnswer.id", target = "checklistAnswerId")
    @Mapping(source = "checklistAnswer.answer", target = "checklistAnswerAnswer")
    ChecklistWorkflowDTO checklistWorkflowToChecklistWorkflowDTO(ChecklistWorkflow checklistWorkflow);

    List<ChecklistWorkflowDTO> checklistWorkflowsToChecklistWorkflowDTOs(List<ChecklistWorkflow> checklistWorkflows);

    @Mapping(source = "checklistId", target = "checklist")
    @Mapping(source = "whoId", target = "who")
    @Mapping(source = "workflowId", target = "workflow")
    @Mapping(source = "checklistAnswerId", target = "checklistAnswer")
    ChecklistWorkflow checklistWorkflowDTOToChecklistWorkflow(ChecklistWorkflowDTO checklistWorkflowDTO);

    List<ChecklistWorkflow> checklistWorkflowDTOsToChecklistWorkflows(List<ChecklistWorkflowDTO> checklistWorkflowDTOs);

    default Checklist checklistFromId(Long id) {
        if (id == null) {
            return null;
        }
        Checklist checklist = new Checklist();
        checklist.setId(id);
        return checklist;
    }

    default Workflow workflowFromId(Long id) {
        if (id == null) {
            return null;
        }
        Workflow workflow = new Workflow();
        workflow.setId(id);
        return workflow;
    }

    default ChecklistAnswer checklistAnswerFromId(Long id) {
        if (id == null) {
            return null;
        }
        ChecklistAnswer checklistAnswer = new ChecklistAnswer();
        checklistAnswer.setId(id);
        return checklistAnswer;
    }
}
