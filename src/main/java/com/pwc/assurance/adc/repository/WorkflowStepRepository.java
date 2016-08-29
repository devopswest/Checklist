package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.WorkflowStep;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkflowStep entity.
 */
@SuppressWarnings("unused")
public interface WorkflowStepRepository extends JpaRepository<WorkflowStep,Long> {

}
