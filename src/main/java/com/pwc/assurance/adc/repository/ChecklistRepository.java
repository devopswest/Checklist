package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.Checklist;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Checklist entity.
 */
@SuppressWarnings("unused")
public interface ChecklistRepository extends JpaRepository<Checklist,Long> {

    @Query("select checklist from Checklist checklist where checklist.owner.login = ?#{principal.username}")
    List<Checklist> findByOwnerIsCurrentUser();

    @Query("select distinct checklist from Checklist checklist left join fetch checklist.checklistAnswers")
    List<Checklist> findAllWithEagerRelationships();

    @Query("select checklist from Checklist checklist left join fetch checklist.checklistAnswers where checklist.id =:id")
    Checklist findOneWithEagerRelationships(@Param("id") Long id);

}
