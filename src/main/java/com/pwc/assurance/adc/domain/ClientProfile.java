package com.pwc.assurance.adc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ClientProfile.
 */
@Entity
@Table(name = "client_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "clientprofile")
public class ClientProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private Client client;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ClientMetadata> metadata = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public ClientProfile client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<ClientMetadata> getMetadata() {
        return metadata;
    }

    public ClientProfile metadata(Set<ClientMetadata> clientMetadata) {
        this.metadata = clientMetadata;
        return this;
    }

    public ClientProfile addClientMetadata(ClientMetadata clientMetadata) {
        metadata.add(clientMetadata);
        clientMetadata.setClient(this);
        return this;
    }

    public ClientProfile removeClientMetadata(ClientMetadata clientMetadata) {
        metadata.remove(clientMetadata);
        clientMetadata.setClient(null);
        return this;
    }

    public void setMetadata(Set<ClientMetadata> clientMetadata) {
        this.metadata = clientMetadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientProfile clientProfile = (ClientProfile) o;
        if(clientProfile.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, clientProfile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientProfile{" +
            "id=" + id +
            '}';
    }
}
