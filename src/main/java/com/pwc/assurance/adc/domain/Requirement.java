package com.pwc.assurance.adc.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * This is the future/next definition where ChecklistTemplate is not longer needed
 * 
 */
@ApiModel(description = ""
    + "This is the future/next definition where ChecklistTemplate is not longer needed"
    + "")
@Entity
@Table(name = "requirement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "requirement")
public class Requirement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "requirement_disclosure_requirement",
               joinColumns = @JoinColumn(name="requirements_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="disclosure_requirements_id", referencedColumnName="ID"))
    private Set<DisclosureRequirement> disclosureRequirements = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Requirement name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Requirement description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<DisclosureRequirement> getDisclosureRequirements() {
        return disclosureRequirements;
    }

    public Requirement disclosureRequirements(Set<DisclosureRequirement> disclosureRequirements) {
        this.disclosureRequirements = disclosureRequirements;
        return this;
    }

    public Requirement addDisclosureRequirement(DisclosureRequirement disclosureRequirement) {
        disclosureRequirements.add(disclosureRequirement);
        disclosureRequirement.getRequirements().add(this);
        return this;
    }

    public Requirement removeDisclosureRequirement(DisclosureRequirement disclosureRequirement) {
        disclosureRequirements.remove(disclosureRequirement);
        disclosureRequirement.getRequirements().remove(this);
        return this;
    }

    public void setDisclosureRequirements(Set<DisclosureRequirement> disclosureRequirements) {
        this.disclosureRequirements = disclosureRequirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Requirement requirement = (Requirement) o;
        if(requirement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, requirement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Requirement{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
