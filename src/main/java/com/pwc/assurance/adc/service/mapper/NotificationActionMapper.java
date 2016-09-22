package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.NotificationActionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity NotificationAction and its DTO NotificationActionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NotificationActionMapper {

    @Mapping(source = "notification.id", target = "notificationId")
    NotificationActionDTO notificationActionToNotificationActionDTO(NotificationAction notificationAction);

    List<NotificationActionDTO> notificationActionsToNotificationActionDTOs(List<NotificationAction> notificationActions);

    @Mapping(source = "notificationId", target = "notification")
    NotificationAction notificationActionDTOToNotificationAction(NotificationActionDTO notificationActionDTO);

    List<NotificationAction> notificationActionDTOsToNotificationActions(List<NotificationActionDTO> notificationActionDTOs);

    default Notification notificationFromId(Long id) {
        if (id == null) {
            return null;
        }
        Notification notification = new Notification();
        notification.setId(id);
        return notification;
    }
}
