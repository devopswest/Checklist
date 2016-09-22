package com.pwc.assurance.adc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ChecklistHistoryChanges.
 */
@Entity
@Table(name = "checklist_history_changes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "checklisthistorychanges")
public class ChecklistHistoryChanges implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "happened")
    private ZonedDateTime happened;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private Checklist checklist;

    @ManyToOne
    private User who;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getHappened() {
        return happened;
    }

    public ChecklistHistoryChanges happened(ZonedDateTime happened) {
        this.happened = happened;
        return this;
    }

    public void setHappened(ZonedDateTime happened) {
        this.happened = happened;
    }

    public String getDescription() {
        return description;
    }

    public ChecklistHistoryChanges description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public ChecklistHistoryChanges checklist(Checklist checklist) {
        this.checklist = checklist;
        return this;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public User getWho() {
        return who;
    }

    public ChecklistHistoryChanges who(User user) {
        this.who = user;
        return this;
    }

    public void setWho(User user) {
        this.who = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChecklistHistoryChanges checklistHistoryChanges = (ChecklistHistoryChanges) o;
        if(checklistHistoryChanges.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, checklistHistoryChanges.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChecklistHistoryChanges{" +
            "id=" + id +
            ", happened='" + happened + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
