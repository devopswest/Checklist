package com.pwc.assurance.adc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A Feature.
 */
@Entity
@Table(name = "feature")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "feature")
public class Feature implements Serializable {

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
    private Set<Feature> children = new HashSet<>();

    @OneToMany(mappedBy = "feature")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FeatureAuthority> authorities = new HashSet<>();

    @ManyToOne
    private Feature parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Feature code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public Feature label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<Feature> getChildren() {
        return children;
    }

    public Feature children(Set<Feature> features) {
        this.children = features;
        return this;
    }

    public Feature addFeature(Feature feature) {
        children.add(feature);
        feature.setParent(this);
        return this;
    }

    public Feature removeFeature(Feature feature) {
        children.remove(feature);
        feature.setParent(null);
        return this;
    }

    public void setChildren(Set<Feature> features) {
        this.children = features;
    }

    public Set<FeatureAuthority> getAuthorities() {
        return authorities;
    }

    public Feature authorities(Set<FeatureAuthority> featureAuthorities) {
        this.authorities = featureAuthorities;
        return this;
    }

    public Feature addFeatureAuthority(FeatureAuthority featureAuthority) {
        authorities.add(featureAuthority);
        featureAuthority.setFeature(this);
        return this;
    }

    public Feature removeFeatureAuthority(FeatureAuthority featureAuthority) {
        authorities.remove(featureAuthority);
        featureAuthority.setFeature(null);
        return this;
    }

    public void setAuthorities(Set<FeatureAuthority> featureAuthorities) {
        this.authorities = featureAuthorities;
    }

    public Feature getParent() {
        return parent;
    }

    public Feature parent(Feature feature) {
        this.parent = feature;
        return this;
    }

    public void setParent(Feature feature) {
        this.parent = feature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Feature feature = (Feature) o;
        if(feature.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, feature.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Feature{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", label='" + label + "'" +
            '}';
    }
}
