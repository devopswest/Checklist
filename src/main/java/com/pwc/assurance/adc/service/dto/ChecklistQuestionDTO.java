package com.pwc.assurance.adc.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import java.util.List;

/**
 * A DTO for the ChecklistQuestion entity.
 */
public class ChecklistQuestionDTO implements Serializable {

    private Long id;

    private String code;

    @Size(min = 1, max = 4000)
    private String description;


    private Long checklistId;


    private String checklistName;

    private Long parentId;


    private String parentDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }


    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long checklistQuestionId) {
        this.parentId = checklistQuestionId;
    }


    public String getParentDescription() {
        return parentDescription;
    }

    public void setParentDescription(String checklistQuestionDescription) {
        this.parentDescription = checklistQuestionDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChecklistQuestionDTO checklistQuestionDTO = (ChecklistQuestionDTO) o;

        if ( ! Objects.equals(id, checklistQuestionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChecklistQuestionDTO{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", description='" + description + "'" +
            '}';
    }


    List<ChecklistQuestionDTO> children;


    public List<ChecklistQuestionDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ChecklistQuestionDTO> children) {
        this.children = children;
    }
}
