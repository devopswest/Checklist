package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ClientTag entity.
 */
public class ClientTagDTO implements Serializable {

    private Long id;

    private String tagValue;


    private Long tagPropertyId;
    

    private String tagPropertyLabel;

    private Long clientId;
    

    private String clientName;

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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClientTagDTO clientTagDTO = (ClientTagDTO) o;

        if ( ! Objects.equals(id, clientTagDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientTagDTO{" +
            "id=" + id +
            ", tagValue='" + tagValue + "'" +
            '}';
    }
}
