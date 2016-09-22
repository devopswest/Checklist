package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.ChecklistTemplate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChecklistTemplate entity.
 */
@SuppressWarnings("unused")
public interface ChecklistTemplateRepository extends JpaRepository<ChecklistTemplate,Long> {

}
