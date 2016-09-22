package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.NotificationAction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NotificationAction entity.
 */
@SuppressWarnings("unused")
public interface NotificationActionRepository extends JpaRepository<NotificationAction,Long> {

}
