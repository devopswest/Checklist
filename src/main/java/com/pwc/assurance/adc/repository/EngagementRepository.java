package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.Engagement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Engagement entity.
 */
@SuppressWarnings("unused")
public interface EngagementRepository extends JpaRepository<Engagement,Long> {

}
