package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.ChecklistQuestionDTO;

import org.mapstruct.*;
import java.util.List;
import java.util.Set;

/**
 * Mapper for the entity ChecklistQuestion and its DTO ChecklistQuestionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChecklistQuestionMapper {

    @Mapping(source = "checklist.id", target = "checklistId")
    @Mapping(source = "checklist.name", target = "checklistName")
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.description", target = "parentDescription")
    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "question.question", target = "questionQuestion")
    @Mapping(source = "children", target = "children")
    ChecklistQuestionDTO checklistQuestionToChecklistQuestionDTO(ChecklistQuestion checklistQuestion);

    List<ChecklistQuestionDTO> checklistQuestionsToChecklistQuestionDTOs(List<ChecklistQuestion> checklistQuestions);
    List<ChecklistQuestionDTO> map(Set<ChecklistQuestion> value);
    
    //@Mapping(target = "children", ignore = true)
    @Mapping(source = "children", target = "children")
    @Mapping(target = "auditQuestionResponses", ignore = true)
    @Mapping(source = "checklistId", target = "checklist")
    @Mapping(source = "parentId", target = "parent")
    @Mapping(source = "questionId", target = "question")
    @Mapping(target = "auditProfiles", ignore = true)
    ChecklistQuestion checklistQuestionDTOToChecklistQuestion(ChecklistQuestionDTO checklistQuestionDTO);

    List<ChecklistQuestion> checklistQuestionDTOsToChecklistQuestions(List<ChecklistQuestionDTO> checklistQuestionDTOs);

    
    default Checklist checklistFromId(Long id) {
        if (id == null) {
            return null;
        }
        Checklist checklist = new Checklist();
        checklist.setId(id);
        return checklist;
    }

    default ChecklistQuestion checklistQuestionFromId(Long id) {
        if (id == null) {
            return null;
        }
        ChecklistQuestion checklistQuestion = new ChecklistQuestion();
        checklistQuestion.setId(id);
        return checklistQuestion;
    }

    default Question questionFromId(Long id) {
        if (id == null) {
            return null;
        }
        Question question = new Question();
        question.setId(id);
        return question;
    }
}
