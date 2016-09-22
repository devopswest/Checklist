package com.pwc.assurance.adc.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Taxonomy entity.
 */
public class TaxonomyDTO implements Serializable {

    private Long id;

    private String code;

    @NotNull
    @Size(min = 1, max = 50)
    private String label;


    private Long parentId;
    

    private String parentLabel;

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
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long taxonomyId) {
        this.parentId = taxonomyId;
    }


    public String getParentLabel() {
        return parentLabel;
    }

    public void setParentLabel(String taxonomyLabel) {
        this.parentLabel = taxonomyLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaxonomyDTO taxonomyDTO = (TaxonomyDTO) o;

        if ( ! Objects.equals(id, taxonomyDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TaxonomyDTO{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", label='" + label + "'" +
            '}';
    }
}
