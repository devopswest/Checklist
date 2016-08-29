package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.Workflow;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Workflow entity.
 */
@SuppressWarnings("unused")
public interface WorkflowRepository extends JpaRepository<Workflow,Long> {

    @Query("select distinct workflow from Workflow workflow left join fetch workflow.workflowSteps")
    List<Workflow> findAllWithEagerRelationships();

    @Query("select workflow from Workflow workflow left join fetch workflow.workflowSteps where workflow.id =:id")
    Workflow findOneWithEagerRelationships(@Param("id") Long id);

}
