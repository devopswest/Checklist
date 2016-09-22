package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Requirement entity.
 */
public class RequirementDTO implements Serializable {

    private Long id;

    private String name;

    private String description;


    private Set<DisclosureRequirementDTO> disclosureRequirements = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<DisclosureRequirementDTO> getDisclosureRequirements() {
        return disclosureRequirements;
    }

    public void setDisclosureRequirements(Set<DisclosureRequirementDTO> disclosureRequirements) {
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

        RequirementDTO requirementDTO = (RequirementDTO) o;

        if ( ! Objects.equals(id, requirementDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RequirementDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
