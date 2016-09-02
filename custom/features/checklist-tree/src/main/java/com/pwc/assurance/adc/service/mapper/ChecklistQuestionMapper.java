package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.ChecklistQuestionDTO;

import org.mapstruct.*;

import java.util.ArrayList;
import java.util.HashSet;
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

    //List<ChecklistQuestionDTO> checklistQuestionsToChecklistQuestionDTOs(List<ChecklistQuestion> checklistQuestions);
    default List<ChecklistQuestionDTO> checklistQuestionsToChecklistQuestionDTOs(List<ChecklistQuestion> checklistQuestions) {
        System.out.println("***** I AM HERE 1");
        if ( checklistQuestions == null ) {
            return null;
        }

        List<ChecklistQuestionDTO> list = new ArrayList<ChecklistQuestionDTO>();
        for ( ChecklistQuestion checklistQuestion : checklistQuestions ) {
            ChecklistQuestionDTO dto = checklistQuestionToChecklistQuestionDTO( checklistQuestion );
            list.add( dto );

            if (checklistQuestion.getChildren() != null && checklistQuestion.getChildren().size()>0) {
                List<ChecklistQuestionDTO> children =  checklistQuestionsToChecklistQuestionDTOs(checklistQuestion.getChildren());
                dto.setChildren(children);
            }
        }

        return list;
    }

    //List<ChecklistQuestionDTO> map(Set<ChecklistQuestion> value);
    default List<ChecklistQuestionDTO> checklistQuestionsToChecklistQuestionDTOs(Set<ChecklistQuestion> checklistQuestions) {
        System.out.println("***** I AM HERE 2");

        if ( checklistQuestions == null ) {
            return null;
        }

        List<ChecklistQuestionDTO> list = new ArrayList<ChecklistQuestionDTO>();
        for ( ChecklistQuestion checklistQuestion : checklistQuestions ) {
            ChecklistQuestionDTO dto = checklistQuestionToChecklistQuestionDTO( checklistQuestion );
            list.add( dto );

            if (checklistQuestion.getChildren() != null && checklistQuestion.getChildren().size()>0) {
                System.out.println("***** I AM HERE 2 -- Have Children");
                List<ChecklistQuestionDTO> children =  checklistQuestionsToChecklistQuestionDTOs(checklistQuestion.getChildren());
                dto.setChildren(children);
            }
        }

        return list;
    }

    //@Mapping(target = "children", ignore = true)
    @Mapping(source = "children", target = "children")
    @Mapping(target = "auditQuestionResponses", ignore = true)
    @Mapping(source = "checklistId", target = "checklist")
    @Mapping(source = "parentId", target = "parent")
    @Mapping(source = "questionId", target = "question")
    @Mapping(target = "auditProfiles", ignore = true)
    ChecklistQuestion checklistQuestionDTOToChecklistQuestion(ChecklistQuestionDTO checklistQuestionDTO);

    //List<ChecklistQuestion> checklistQuestionDTOsToChecklistQuestions(List<ChecklistQuestionDTO> checklistQuestionDTOs);
    default List<ChecklistQuestion> checklistQuestionDTOsToChecklistQuestions(List<ChecklistQuestionDTO> checklistQuestionDTOs) {
        if ( checklistQuestionDTOs == null ) {
            return null;
        }

        List<ChecklistQuestion> list_ = new ArrayList<ChecklistQuestion>();
        for ( ChecklistQuestionDTO checklistQuestionDTO : checklistQuestionDTOs ) {
            ChecklistQuestion obj = checklistQuestionDTOToChecklistQuestion( checklistQuestionDTO );
            list_.add( obj );

            if (checklistQuestionDTO.getChildren() != null && checklistQuestionDTO.getChildren().size()>0) {
                List<ChecklistQuestion> children =  checklistQuestionDTOsToChecklistQuestions(checklistQuestionDTO.getChildren());
                obj.setChildren(listToSet(children));
            }


        }

        return list_;
    }

    default Set<ChecklistQuestion> listToSet(List<ChecklistQuestion> list) {
        if ( list == null ) {
            return null;
        }

        Set<ChecklistQuestion> set = new HashSet<ChecklistQuestion>();
        for ( ChecklistQuestion checklistQuestion : list ) {
            set.add( checklistQuestion ) ;
        }

        return set;
    }
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
