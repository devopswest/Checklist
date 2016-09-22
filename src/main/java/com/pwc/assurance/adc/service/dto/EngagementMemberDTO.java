package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pwc.assurance.adc.domain.enumeration.EngagementAuthorities;

/**
 * A DTO for the EngagementMember entity.
 */
public class EngagementMemberDTO implements Serializable {

    private Long id;

    private EngagementAuthorities authority;


    private Long userId;
    

    private String userLogin;

    private Long engagementId;
    

    private String engagementDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public EngagementAuthorities getAuthority() {
        return authority;
    }

    public void setAuthority(EngagementAuthorities authority) {
        this.authority = authority;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(Long engagementId) {
        this.engagementId = engagementId;
    }


    public String getEngagementDescription() {
        return engagementDescription;
    }

    public void setEngagementDescription(String engagementDescription) {
        this.engagementDescription = engagementDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EngagementMemberDTO engagementMemberDTO = (EngagementMemberDTO) o;

        if ( ! Objects.equals(id, engagementMemberDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EngagementMemberDTO{" +
            "id=" + id +
            ", authority='" + authority + "'" +
            '}';
    }
}
