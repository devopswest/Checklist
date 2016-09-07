package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.AuditProfile;
import org.javers.spring.annotation.JaversSpringDataAuditable;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
/**
 * Spring Data JPA repository for the AuditProfile entity.
 */
@SuppressWarnings("unused")
@JaversSpringDataAuditable
public interface AuditProfileRepository extends JpaRepository<AuditProfile,Long> {

    @Query("select distinct auditProfile from AuditProfile auditProfile left join fetch auditProfile.auditQuestionResponses")
    List<AuditProfile> findAllWithEagerRelationships();

    @Query(value = "select distinct auditProfile from AuditProfile auditProfile left join fetch auditProfile.auditQuestionResponses",
            countQuery = "select count(auditProfile) from AuditProfile auditProfile")
    Page<AuditProfile> findAllWithEagerRelationships(Pageable pageable);

    @Query("select auditProfile from AuditProfile auditProfile left join fetch auditProfile.auditQuestionResponses where auditProfile.id =:id")
    AuditProfile findOneWithEagerRelationships(@Param("id") Long id);

}
