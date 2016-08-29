package com.pwc.assurance.adc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A AuditProfileLogEntry.
 */
@Entity
@Table(name = "audit_profile_log_entry")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "auditprofilelogentry")
public class AuditProfileLogEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "happened")
    private ZonedDateTime happened;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private AuditProfile auditProfile;

    @OneToOne
    @JoinColumn(unique = true)
    private User who;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getHappened() {
        return happened;
    }

    public AuditProfileLogEntry happened(ZonedDateTime happened) {
        this.happened = happened;
        return this;
    }

    public void setHappened(ZonedDateTime happened) {
        this.happened = happened;
    }

    public String getDescription() {
        return description;
    }

    public AuditProfileLogEntry description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AuditProfile getAuditProfile() {
        return auditProfile;
    }

    public AuditProfileLogEntry auditProfile(AuditProfile auditProfile) {
        this.auditProfile = auditProfile;
        return this;
    }

    public void setAuditProfile(AuditProfile auditProfile) {
        this.auditProfile = auditProfile;
    }

    public User getWho() {
        return who;
    }

    public AuditProfileLogEntry who(User user) {
        this.who = user;
        return this;
    }

    public void setWho(User user) {
        this.who = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuditProfileLogEntry auditProfileLogEntry = (AuditProfileLogEntry) o;
        if(auditProfileLogEntry.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, auditProfileLogEntry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AuditProfileLogEntry{" +
            "id=" + id +
            ", happened='" + happened + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
