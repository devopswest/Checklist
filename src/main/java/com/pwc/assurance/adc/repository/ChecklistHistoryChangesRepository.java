package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.ChecklistHistoryChanges;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChecklistHistoryChanges entity.
 */
@SuppressWarnings("unused")
public interface ChecklistHistoryChangesRepository extends JpaRepository<ChecklistHistoryChanges,Long> {

    @Query("select checklistHistoryChanges from ChecklistHistoryChanges checklistHistoryChanges where checklistHistoryChanges.who.login = ?#{principal.username}")
    List<ChecklistHistoryChanges> findByWhoIsCurrentUser();

}
