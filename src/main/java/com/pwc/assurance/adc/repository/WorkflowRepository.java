package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.Workflow;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Workflow entity.
 */
@SuppressWarnings("unused")
public interface WorkflowRepository extends JpaRepository<Workflow,Long> {

}
