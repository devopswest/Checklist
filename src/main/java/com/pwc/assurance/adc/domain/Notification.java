package com.pwc.assurance.adc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Notifications                                                               
 * 
 */
@ApiModel(description = ""
    + "Notifications                                                          "
    + "")
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "content")
    private String content;

    @OneToOne
    @JoinColumn(unique = true)
    private User from;

    @OneToOne
    @JoinColumn(unique = true)
    private User to;

    @OneToMany(mappedBy = "notification")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NotificationAction> actions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public Notification content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getFrom() {
        return from;
    }

    public Notification from(User user) {
        this.from = user;
        return this;
    }

    public void setFrom(User user) {
        this.from = user;
    }

    public User getTo() {
        return to;
    }

    public Notification to(User user) {
        this.to = user;
        return this;
    }

    public void setTo(User user) {
        this.to = user;
    }

    public Set<NotificationAction> getActions() {
        return actions;
    }

    public Notification actions(Set<NotificationAction> notificationActions) {
        this.actions = notificationActions;
        return this;
    }

    public Notification addNotificationAction(NotificationAction notificationAction) {
        actions.add(notificationAction);
        notificationAction.setNotification(this);
        return this;
    }

    public Notification removeNotificationAction(NotificationAction notificationAction) {
        actions.remove(notificationAction);
        notificationAction.setNotification(null);
        return this;
    }

    public void setActions(Set<NotificationAction> notificationActions) {
        this.actions = notificationActions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notification notification = (Notification) o;
        if(notification.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notification.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Notification{" +
            "id=" + id +
            ", content='" + content + "'" +
            '}';
    }
}
