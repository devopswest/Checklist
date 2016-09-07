package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.ChecklistQuestionDTO;

import org.mapstruct.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
/**
 * Mapper for the entity ChecklistQuestion and its DTO ChecklistQuestionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChecklistQuestionMapper {

    @Mapping(source = "checklist.id", target = "checklistId")
    @Mapping(source = "checklist.name", target = "checklistName")
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.description", target = "parentDescription")
    ChecklistQuestionDTO checklistQuestionToChecklistQuestionDTO(ChecklistQuestion checklistQuestion);

    //List<ChecklistQuestionDTO> checklistQuestionsToChecklistQuestionDTOs(List<ChecklistQuestion> checklistQuestions);

    //@Mapping(target = "children", ignore = true)
    @Mapping(source = "checklistId", target = "checklist")
    @Mapping(source = "parentId", target = "parent")
    ChecklistQuestion checklistQuestionDTOToChecklistQuestion(ChecklistQuestionDTO checklistQuestionDTO);

    //List<ChecklistQuestion> checklistQuestionDTOsToChecklistQuestions(List<ChecklistQuestionDTO> checklistQuestionDTOs);

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

    ////
    default List<ChecklistQuestionDTO> checklistQuestionsToChecklistQuestionDTOs(List<ChecklistQuestion> checklistQuestions) {
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

    default List<ChecklistQuestionDTO> checklistQuestionsToChecklistQuestionDTOs(Set<ChecklistQuestion> checklistQuestions) {

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
}
