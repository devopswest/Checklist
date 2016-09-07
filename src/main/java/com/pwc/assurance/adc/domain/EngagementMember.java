package com.pwc.assurance.adc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.EngagementAuthorities;

/**
 * A EngagementMember.
 */
@Entity
@Table(name = "engagement_member")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "engagementmember")
public class EngagementMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private EngagementAuthorities authority;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    private Engagement engagement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EngagementAuthorities getAuthority() {
        return authority;
    }

    public EngagementMember authority(EngagementAuthorities authority) {
        this.authority = authority;
        return this;
    }

    public void setAuthority(EngagementAuthorities authority) {
        this.authority = authority;
    }

    public User getUser() {
        return user;
    }

    public EngagementMember user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Engagement getEngagement() {
        return engagement;
    }

    public EngagementMember engagement(Engagement engagement) {
        this.engagement = engagement;
        return this;
    }

    public void setEngagement(Engagement engagement) {
        this.engagement = engagement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EngagementMember engagementMember = (EngagementMember) o;
        if(engagementMember.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, engagementMember.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EngagementMember{" +
            "id=" + id +
            ", authority='" + authority + "'" +
            '}';
    }
}
