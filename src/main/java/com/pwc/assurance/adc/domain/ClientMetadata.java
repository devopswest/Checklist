package com.pwc.assurance.adc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ClientMetadata.
 */
@Entity
@Table(name = "client_metadata")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "clientmetadata")
public class ClientMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "metadata_value")
    private String metadataValue;

    @ManyToOne
    private Taxonomy metadataProperty;

    @ManyToOne
    private ClientProfile client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetadataValue() {
        return metadataValue;
    }

    public ClientMetadata metadataValue(String metadataValue) {
        this.metadataValue = metadataValue;
        return this;
    }

    public void setMetadataValue(String metadataValue) {
        this.metadataValue = metadataValue;
    }

    public Taxonomy getMetadataProperty() {
        return metadataProperty;
    }

    public ClientMetadata metadataProperty(Taxonomy taxonomy) {
        this.metadataProperty = taxonomy;
        return this;
    }

    public void setMetadataProperty(Taxonomy taxonomy) {
        this.metadataProperty = taxonomy;
    }

    public ClientProfile getClient() {
        return client;
    }

    public ClientMetadata client(ClientProfile clientProfile) {
        this.client = clientProfile;
        return this;
    }

    public void setClient(ClientProfile clientProfile) {
        this.client = clientProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientMetadata clientMetadata = (ClientMetadata) o;
        if(clientMetadata.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, clientMetadata.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientMetadata{" +
            "id=" + id +
            ", metadataValue='" + metadataValue + "'" +
            '}';
    }
}
