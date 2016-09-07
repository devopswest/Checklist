package com.pwc.assurance.adc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.ApplicationAuthorities;

/**
 * A FeatureAuthority.
 */
@Entity
@Table(name = "feature_authority")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "featureauthority")
public class FeatureAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private ApplicationAuthorities authority;

    @ManyToOne
    private Feature feature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationAuthorities getAuthority() {
        return authority;
    }

    public FeatureAuthority authority(ApplicationAuthorities authority) {
        this.authority = authority;
        return this;
    }

    public void setAuthority(ApplicationAuthorities authority) {
        this.authority = authority;
    }

    public Feature getFeature() {
        return feature;
    }

    public FeatureAuthority feature(Feature feature) {
        this.feature = feature;
        return this;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FeatureAuthority featureAuthority = (FeatureAuthority) o;
        if(featureAuthority.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, featureAuthority.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FeatureAuthority{" +
            "id=" + id +
            ", authority='" + authority + "'" +
            '}';
    }
}
