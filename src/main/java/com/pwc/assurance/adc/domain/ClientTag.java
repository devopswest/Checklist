package com.pwc.assurance.adc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ClientTag.
 */
@Entity
@Table(name = "client_tag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "clienttag")
public class ClientTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "tag_value")
    private String tagValue;

    @ManyToOne
    private Taxonomy tagProperty;

    @ManyToOne
    private Client client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagValue() {
        return tagValue;
    }

    public ClientTag tagValue(String tagValue) {
        this.tagValue = tagValue;
        return this;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public Taxonomy getTagProperty() {
        return tagProperty;
    }

    public ClientTag tagProperty(Taxonomy taxonomy) {
        this.tagProperty = taxonomy;
        return this;
    }

    public void setTagProperty(Taxonomy taxonomy) {
        this.tagProperty = taxonomy;
    }

    public Client getClient() {
        return client;
    }

    public ClientTag client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientTag clientTag = (ClientTag) o;
        if(clientTag.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, clientTag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientTag{" +
            "id=" + id +
            ", tagValue='" + tagValue + "'" +
            '}';
    }
}
