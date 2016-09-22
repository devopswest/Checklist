package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.NotificationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Notification and its DTO NotificationDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, UserMapper.class, })
public interface NotificationMapper {

    @Mapping(source = "from.id", target = "fromId")
    @Mapping(source = "from.login", target = "fromLogin")
    @Mapping(source = "to.id", target = "toId")
    @Mapping(source = "to.login", target = "toLogin")
    NotificationDTO notificationToNotificationDTO(Notification notification);

    List<NotificationDTO> notificationsToNotificationDTOs(List<Notification> notifications);

    @Mapping(source = "fromId", target = "from")
    @Mapping(source = "toId", target = "to")
    @Mapping(target = "actions", ignore = true)
    Notification notificationDTOToNotification(NotificationDTO notificationDTO);

    List<Notification> notificationDTOsToNotifications(List<NotificationDTO> notificationDTOs);
}
