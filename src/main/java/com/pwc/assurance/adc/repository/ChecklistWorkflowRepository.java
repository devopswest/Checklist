package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.ChecklistWorkflow;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChecklistWorkflow entity.
 */
@SuppressWarnings("unused")
public interface ChecklistWorkflowRepository extends JpaRepository<ChecklistWorkflow,Long> {

    @Query("select checklistWorkflow from ChecklistWorkflow checklistWorkflow where checklistWorkflow.who.login = ?#{principal.username}")
    List<ChecklistWorkflow> findByWhoIsCurrentUser();

}
