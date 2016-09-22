package com.pwc.assurance.adc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * ClientTODO: Additional Tag. LOS, etc, Client Profile ( i have cache, recibables, invenmtory this drive the requirements)
 * 
 */
@ApiModel(description = ""
    + "ClientTODO: Additional Tag. LOS, etc, Client Profile ( i have cache, recibables, invenmtory this drive the requirements)"
    + "")
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "duns")
    private String duns;

    @Column(name = "party_id")
    private String partyId;

    @Column(name = "ces_id")
    private String cesId;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ClientTag> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Client code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Client name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuns() {
        return duns;
    }

    public Client duns(String duns) {
        this.duns = duns;
        return this;
    }

    public void setDuns(String duns) {
        this.duns = duns;
    }

    public String getPartyId() {
        return partyId;
    }

    public Client partyId(String partyId) {
        this.partyId = partyId;
        return this;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getCesId() {
        return cesId;
    }

    public Client cesId(String cesId) {
        this.cesId = cesId;
        return this;
    }

    public void setCesId(String cesId) {
        this.cesId = cesId;
    }

    public Set<ClientTag> getTags() {
        return tags;
    }

    public Client tags(Set<ClientTag> clientTags) {
        this.tags = clientTags;
        return this;
    }

    public Client addClientTag(ClientTag clientTag) {
        tags.add(clientTag);
        clientTag.setClient(this);
        return this;
    }

    public Client removeClientTag(ClientTag clientTag) {
        tags.remove(clientTag);
        clientTag.setClient(null);
        return this;
    }

    public void setTags(Set<ClientTag> clientTags) {
        this.tags = clientTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client client = (Client) o;
        if(client.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", name='" + name + "'" +
            ", duns='" + duns + "'" +
            ", partyId='" + partyId + "'" +
            ", cesId='" + cesId + "'" +
            '}';
    }
}
