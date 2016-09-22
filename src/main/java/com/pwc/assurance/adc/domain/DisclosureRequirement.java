package com.pwc.assurance.adc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DisclosureRequirement.
 */
@Entity
@Table(name = "disclosure_requirement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "disclosurerequirement")
public class DisclosureRequirement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code")
    private String code;

    @Size(min = 1, max = 4000)
    @Column(name = "description", length = 4000)
    private String description;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DisclosureRequirement> children = new HashSet<>();

    @OneToMany(mappedBy = "disclosureRequirement")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DisclosureRequirementTag> tags = new HashSet<>();

    @ManyToOne
    private ChecklistTemplate checklistTemplate;

    @ManyToOne
    private DisclosureRequirement parent;

    @ManyToMany(mappedBy = "disclosureRequirements")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Requirement> requirements = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public DisclosureRequirement code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public DisclosureRequirement description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<DisclosureRequirement> getChildren() {
        return children;
    }

    public DisclosureRequirement children(Set<DisclosureRequirement> disclosureRequirements) {
        this.children = disclosureRequirements;
        return this;
    }

    public DisclosureRequirement addDisclosureRequirement(DisclosureRequirement disclosureRequirement) {
        children.add(disclosureRequirement);
        disclosureRequirement.setParent(this);
        return this;
    }

    public DisclosureRequirement removeDisclosureRequirement(DisclosureRequirement disclosureRequirement) {
        children.remove(disclosureRequirement);
        disclosureRequirement.setParent(null);
        return this;
    }

    public void setChildren(Set<DisclosureRequirement> disclosureRequirements) {
        this.children = disclosureRequirements;
    }

    public Set<DisclosureRequirementTag> getTags() {
        return tags;
    }

    public DisclosureRequirement tags(Set<DisclosureRequirementTag> disclosureRequirementTags) {
        this.tags = disclosureRequirementTags;
        return this;
    }

    public DisclosureRequirement addDisclosureRequirementTag(DisclosureRequirementTag disclosureRequirementTag) {
        tags.add(disclosureRequirementTag);
        disclosureRequirementTag.setDisclosureRequirement(this);
        return this;
    }

    public DisclosureRequirement removeDisclosureRequirementTag(DisclosureRequirementTag disclosureRequirementTag) {
        tags.remove(disclosureRequirementTag);
        disclosureRequirementTag.setDisclosureRequirement(null);
        return this;
    }

    public void setTags(Set<DisclosureRequirementTag> disclosureRequirementTags) {
        this.tags = disclosureRequirementTags;
    }

    public ChecklistTemplate getChecklistTemplate() {
        return checklistTemplate;
    }

    public DisclosureRequirement checklistTemplate(ChecklistTemplate checklistTemplate) {
        this.checklistTemplate = checklistTemplate;
        return this;
    }

    public void setChecklistTemplate(ChecklistTemplate checklistTemplate) {
        this.checklistTemplate = checklistTemplate;
    }

    public DisclosureRequirement getParent() {
        return parent;
    }

    public DisclosureRequirement parent(DisclosureRequirement disclosureRequirement) {
        this.parent = disclosureRequirement;
        return this;
    }

    public void setParent(DisclosureRequirement disclosureRequirement) {
        this.parent = disclosureRequirement;
    }

    public Set<Requirement> getRequirements() {
        return requirements;
    }

    public DisclosureRequirement requirements(Set<Requirement> requirements) {
        this.requirements = requirements;
        return this;
    }

    public DisclosureRequirement addRequirement(Requirement requirement) {
        requirements.add(requirement);
        requirement.getDisclosureRequirements().add(this);
        return this;
    }

    public DisclosureRequirement removeRequirement(Requirement requirement) {
        requirements.remove(requirement);
        requirement.getDisclosureRequirements().remove(this);
        return this;
    }

    public void setRequirements(Set<Requirement> requirements) {
        this.requirements = requirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DisclosureRequirement disclosureRequirement = (DisclosureRequirement) o;
        if(disclosureRequirement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, disclosureRequirement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DisclosureRequirement{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
