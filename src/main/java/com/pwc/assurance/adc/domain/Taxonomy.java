package com.pwc.assurance.adc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Lookups                                                                     
 * 
 */
@ApiModel(description = ""
    + "Lookups                                                                "
    + "")
@Entity
@Table(name = "taxonomy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "taxonomy")
public class Taxonomy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "label", length = 50, nullable = false)
    private String label;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Taxonomy> children = new HashSet<>();

    @OneToMany(mappedBy = "licenseType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<License> licenses = new HashSet<>();

    @ManyToOne
    private Taxonomy parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Taxonomy code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public Taxonomy label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<Taxonomy> getChildren() {
        return children;
    }

    public Taxonomy children(Set<Taxonomy> taxonomies) {
        this.children = taxonomies;
        return this;
    }

    public Taxonomy addTaxonomy(Taxonomy taxonomy) {
        children.add(taxonomy);
        taxonomy.setParent(this);
        return this;
    }

    public Taxonomy removeTaxonomy(Taxonomy taxonomy) {
        children.remove(taxonomy);
        taxonomy.setParent(null);
        return this;
    }

    public void setChildren(Set<Taxonomy> taxonomies) {
        this.children = taxonomies;
    }

    public Set<License> getLicenses() {
        return licenses;
    }

    public Taxonomy licenses(Set<License> licenses) {
        this.licenses = licenses;
        return this;
    }

    public Taxonomy addLicense(License license) {
        licenses.add(license);
        license.setLicenseType(this);
        return this;
    }

    public Taxonomy removeLicense(License license) {
        licenses.remove(license);
        license.setLicenseType(null);
        return this;
    }

    public void setLicenses(Set<License> licenses) {
        this.licenses = licenses;
    }

    public Taxonomy getParent() {
        return parent;
    }

    public Taxonomy parent(Taxonomy taxonomy) {
        this.parent = taxonomy;
        return this;
    }

    public void setParent(Taxonomy taxonomy) {
        this.parent = taxonomy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Taxonomy taxonomy = (Taxonomy) o;
        if(taxonomy.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, taxonomy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Taxonomy{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", label='" + label + "'" +
            '}';
    }
}
