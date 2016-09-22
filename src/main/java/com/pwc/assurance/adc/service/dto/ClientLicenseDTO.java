package com.pwc.assurance.adc.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ClientLicense entity.
 */
public class ClientLicenseDTO implements Serializable {

    private Long id;

    private String contactName;

    private String contactEmail;

    private ZonedDateTime expirationDate;

    @Size(min = 1, max = 4000)
    private String activationToken;


    private Long clientId;
    

    private String clientName;

    private Long clientLicenseTypeId;
    

    private String clientLicenseTypeLabel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
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

    public Long getClientLicenseTypeId() {
        return clientLicenseTypeId;
    }

    public void setClientLicenseTypeId(Long taxonomyId) {
        this.clientLicenseTypeId = taxonomyId;
    }


    public String getClientLicenseTypeLabel() {
        return clientLicenseTypeLabel;
    }

    public void setClientLicenseTypeLabel(String taxonomyLabel) {
        this.clientLicenseTypeLabel = taxonomyLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClientLicenseDTO clientLicenseDTO = (ClientLicenseDTO) o;

        if ( ! Objects.equals(id, clientLicenseDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientLicenseDTO{" +
            "id=" + id +
            ", contactName='" + contactName + "'" +
            ", contactEmail='" + contactEmail + "'" +
            ", expirationDate='" + expirationDate + "'" +
            ", activationToken='" + activationToken + "'" +
            '}';
    }
}
