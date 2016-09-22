package com.pwc.assurance.adc.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ChecklistHistoryChanges entity.
 */
public class ChecklistHistoryChangesDTO implements Serializable {

    private Long id;

    private ZonedDateTime happened;

    private String description;


    private Long checklistId;
    
    private Long whoId;
    

    private String whoLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getHappened() {
        return happened;
    }

    public void setHappened(ZonedDateTime happened) {
        this.happened = happened;
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

    public Long getWhoId() {
        return whoId;
    }

    public void setWhoId(Long userId) {
        this.whoId = userId;
    }


    public String getWhoLogin() {
        return whoLogin;
    }

    public void setWhoLogin(String userLogin) {
        this.whoLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChecklistHistoryChangesDTO checklistHistoryChangesDTO = (ChecklistHistoryChangesDTO) o;

        if ( ! Objects.equals(id, checklistHistoryChangesDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChecklistHistoryChangesDTO{" +
            "id=" + id +
            ", happened='" + happened + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
