package com.pwc.assurance.adc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pwc.assurance.adc.domain.AuditProfile;

/**
 * Spring Data JPA repository for the AuditProfile entity.
 */
@SuppressWarnings("unused")
public interface AuditProfileRepository extends JpaRepository<AuditProfile,Long> {

    @Query(value = "select distinct auditProfile from AuditProfile auditProfile left join fetch auditProfile.questions left join fetch auditProfile.auditQuestionResponses",
            countQuery = "select count(auditProfile) from AuditProfile auditProfile")
    Page<AuditProfile> findAllWithEagerRelationships(Pageable pageable);


    @Query("select auditProfile from AuditProfile auditProfile left join fetch auditProfile.questions left join fetch auditProfile.auditQuestionResponses where auditProfile.id =:id")
    AuditProfile findOneWithEagerRelationships(@Param("id") Long id);
}
