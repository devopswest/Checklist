package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.AuditProfile;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the AuditProfile entity.
 */
@SuppressWarnings("unused")
public interface AuditProfileRepository extends JpaRepository<AuditProfile,Long> {

    @Query("select distinct auditProfile from AuditProfile auditProfile left join fetch auditProfile.questions left join fetch auditProfile.auditQuestionResponses")
    List<AuditProfile> findAllWithEagerRelationships();

    @Query("select auditProfile from AuditProfile auditProfile left join fetch auditProfile.questions left join fetch auditProfile.auditQuestionResponses where auditProfile.id =:id")
    AuditProfile findOneWithEagerRelationships(@Param("id") Long id);

}
