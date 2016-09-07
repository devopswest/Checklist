package com.pwc.assurance.adc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.ResponseStatus;

/**
 * Engagement                                                                  
 * 
 */
@ApiModel(description = ""
    + "Engagement                                                             "
    + "")
@Entity
@Table(name = "engagement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "engagement")
public class Engagement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fiscal_year")
    private String fiscalYear;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ResponseStatus status;

    @OneToMany(mappedBy = "engagement")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EngagementMember> members = new HashSet<>();

    @ManyToOne
    private Client client;

    @ManyToOne
    private Checklist checklist;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public Engagement fiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
        return this;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getDescription() {
        return description;
    }

    public Engagement description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public Engagement status(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public Set<EngagementMember> getMembers() {
        return members;
    }

    public Engagement members(Set<EngagementMember> engagementMembers) {
        this.members = engagementMembers;
        return this;
    }

    public Engagement addEngagementMember(EngagementMember engagementMember) {
        members.add(engagementMember);
        engagementMember.setEngagement(this);
        return this;
    }

    public Engagement removeEngagementMember(EngagementMember engagementMember) {
        members.remove(engagementMember);
        engagementMember.setEngagement(null);
        return this;
    }

    public void setMembers(Set<EngagementMember> engagementMembers) {
        this.members = engagementMembers;
    }

    public Client getClient() {
        return client;
    }

    public Engagement client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public Engagement checklist(Checklist checklist) {
        this.checklist = checklist;
        return this;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Engagement engagement = (Engagement) o;
        if(engagement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, engagement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Engagement{" +
            "id=" + id +
            ", fiscalYear='" + fiscalYear + "'" +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
