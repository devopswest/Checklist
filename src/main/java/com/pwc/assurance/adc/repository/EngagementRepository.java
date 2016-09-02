package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.Engagement;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Engagement entity.
 */
@SuppressWarnings("unused")
public interface EngagementRepository extends JpaRepository<Engagement,Long> {

    @Query("select distinct engagement from Engagement engagement left join fetch engagement.engagementMembers")
    List<Engagement> findAllWithEagerRelationships();

    @Query("select engagement from Engagement engagement left join fetch engagement.engagementMembers where engagement.id =:id")
    Engagement findOneWithEagerRelationships(@Param("id") Long id);

}
