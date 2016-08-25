package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.AuditQuestionResponse;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AuditQuestionResponse entity.
 */
@SuppressWarnings("unused")
public interface AuditQuestionResponseRepository extends JpaRepository<AuditQuestionResponse,Long> {

}
