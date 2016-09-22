package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Notification entity.
 */
public class NotificationDTO implements Serializable {

    private Long id;

    private String content;


    private Long fromId;
    

    private String fromLogin;

    private Long toId;
    

    private String toLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long userId) {
        this.fromId = userId;
    }


    public String getFromLogin() {
        return fromLogin;
    }

    public void setFromLogin(String userLogin) {
        this.fromLogin = userLogin;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long userId) {
        this.toId = userId;
    }


    public String getToLogin() {
        return toLogin;
    }

    public void setToLogin(String userLogin) {
        this.toLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;

        if ( ! Objects.equals(id, notificationDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + id +
            ", content='" + content + "'" +
            '}';
    }
}
