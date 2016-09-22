package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.ApplicationAuthorities;

/**
 * A DTO for the FeatureAuthority entity.
 */
public class FeatureAuthorityDTO implements Serializable {

    private Long id;

    private ApplicationAuthorities authority;


    private Long featureId;
    

    private String featureLabel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ApplicationAuthorities getAuthority() {
        return authority;
    }

    public void setAuthority(ApplicationAuthorities authority) {
        this.authority = authority;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }


    public String getFeatureLabel() {
        return featureLabel;
    }

    public void setFeatureLabel(String featureLabel) {
        this.featureLabel = featureLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FeatureAuthorityDTO featureAuthorityDTO = (FeatureAuthorityDTO) o;

        if ( ! Objects.equals(id, featureAuthorityDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FeatureAuthorityDTO{" +
            "id=" + id +
            ", authority='" + authority + "'" +
            '}';
    }
}
