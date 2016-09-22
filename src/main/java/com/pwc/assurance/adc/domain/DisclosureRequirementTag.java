package com.pwc.assurance.adc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DisclosureRequirementTag.
 */
@Entity
@Table(name = "disclosure_requirement_tag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "disclosurerequirementtag")
public class DisclosureRequirementTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "tag_value")
    private String tagValue;

    @ManyToOne
    private Taxonomy tagProperty;

    @ManyToOne
    private DisclosureRequirement disclosureRequirement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagValue() {
        return tagValue;
    }

    public DisclosureRequirementTag tagValue(String tagValue) {
        this.tagValue = tagValue;
        return this;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public Taxonomy getTagProperty() {
        return tagProperty;
    }

    public DisclosureRequirementTag tagProperty(Taxonomy taxonomy) {
        this.tagProperty = taxonomy;
        return this;
    }

    public void setTagProperty(Taxonomy taxonomy) {
        this.tagProperty = taxonomy;
    }

    public DisclosureRequirement getDisclosureRequirement() {
        return disclosureRequirement;
    }

    public DisclosureRequirementTag disclosureRequirement(DisclosureRequirement disclosureRequirement) {
        this.disclosureRequirement = disclosureRequirement;
        return this;
    }

    public void setDisclosureRequirement(DisclosureRequirement disclosureRequirement) {
        this.disclosureRequirement = disclosureRequirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DisclosureRequirementTag disclosureRequirementTag = (DisclosureRequirementTag) o;
        if(disclosureRequirementTag.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, disclosureRequirementTag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DisclosureRequirementTag{" +
            "id=" + id +
            ", tagValue='" + tagValue + "'" +
            '}';
    }
}
