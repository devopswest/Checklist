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

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private EngagementAuthorities authority;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany(mappedBy = "engagementMembers")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Engagement> engagements = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public EngagementMember firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public EngagementMember lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public EngagementMember phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public EngagementMember email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Set<Engagement> getEngagements() {
        return engagements;
    }

    public EngagementMember engagements(Set<Engagement> engagements) {
        this.engagements = engagements;
        return this;
    }

    public EngagementMember addEngagement(Engagement engagement) {
        engagements.add(engagement);
        engagement.getEngagementMembers().add(this);
        return this;
    }

    public EngagementMember removeEngagement(Engagement engagement) {
        engagements.remove(engagement);
        engagement.getEngagementMembers().remove(this);
        return this;
    }

    public void setEngagements(Set<Engagement> engagements) {
        this.engagements = engagements;
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
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", phone='" + phone + "'" +
            ", email='" + email + "'" +
            ", authority='" + authority + "'" +
            '}';
    }
}
