package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.Requirement;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Requirement entity.
 */
@SuppressWarnings("unused")
public interface RequirementRepository extends JpaRepository<Requirement,Long> {

    @Query("select distinct requirement from Requirement requirement left join fetch requirement.questions")
    List<Requirement> findAllWithEagerRelationships();

    @Query("select requirement from Requirement requirement left join fetch requirement.questions where requirement.id =:id")
    Requirement findOneWithEagerRelationships(@Param("id") Long id);

}
