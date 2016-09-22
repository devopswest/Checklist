package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.EngagementChecklistTemplate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EngagementChecklistTemplate entity.
 */
@SuppressWarnings("unused")
public interface EngagementChecklistTemplateRepository extends JpaRepository<EngagementChecklistTemplate,Long> {

}
