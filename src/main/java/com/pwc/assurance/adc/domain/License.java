package com.pwc.assurance.adc.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Licenses                                                                    
 * 
 */
@ApiModel(description = ""
    + "Licenses                                                               "
    + "")
@Entity
@Table(name = "license")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "license")
public class License implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "expiration_date")
    private ZonedDateTime expirationDate;

    @Size(min = 1, max = 4000)
    @Column(name = "activation_token", length = 4000)
    private String activationToken;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Taxonomy licenseType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public License contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public License contactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public License expirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public License activationToken(String activationToken) {
        this.activationToken = activationToken;
        return this;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public Client getClient() {
        return client;
    }

    public License client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Taxonomy getLicenseType() {
        return licenseType;
    }

    public License licenseType(Taxonomy taxonomy) {
        this.licenseType = taxonomy;
        return this;
    }

    public void setLicenseType(Taxonomy taxonomy) {
        this.licenseType = taxonomy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        License license = (License) o;
        if(license.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, license.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "License{" +
            "id=" + id +
            ", contactName='" + contactName + "'" +
            ", contactEmail='" + contactEmail + "'" +
            ", expirationDate='" + expirationDate + "'" +
            ", activationToken='" + activationToken + "'" +
            '}';
    }
}
