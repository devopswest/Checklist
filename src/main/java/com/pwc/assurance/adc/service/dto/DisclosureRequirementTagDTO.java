package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the DisclosureRequirementTag entity.
 */
public class DisclosureRequirementTagDTO implements Serializable {

    private Long id;

    private String tagValue;


    private Long tagPropertyId;
    

    private String tagPropertyLabel;

    private Long disclosureRequirementId;
    

    private String disclosureRequirementDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public Long getTagPropertyId() {
        return tagPropertyId;
    }

    public void setTagPropertyId(Long taxonomyId) {
        this.tagPropertyId = taxonomyId;
    }


    public String getTagPropertyLabel() {
        return tagPropertyLabel;
    }

    public void setTagPropertyLabel(String taxonomyLabel) {
        this.tagPropertyLabel = taxonomyLabel;
    }

    public Long getDisclosureRequirementId() {
        return disclosureRequirementId;
    }

    public void setDisclosureRequirementId(Long disclosureRequirementId) {
        this.disclosureRequirementId = disclosureRequirementId;
    }


    public String getDisclosureRequirementDescription() {
        return disclosureRequirementDescription;
    }

    public void setDisclosureRequirementDescription(String disclosureRequirementDescription) {
        this.disclosureRequirementDescription = disclosureRequirementDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DisclosureRequirementTagDTO disclosureRequirementTagDTO = (DisclosureRequirementTagDTO) o;

        if ( ! Objects.equals(id, disclosureRequirementTagDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DisclosureRequirementTagDTO{" +
            "id=" + id +
            ", tagValue='" + tagValue + "'" +
            '}';
    }
}
