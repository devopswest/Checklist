package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.AuditProfileLogEntry;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AuditProfileLogEntry entity.
 */
@SuppressWarnings("unused")
public interface AuditProfileLogEntryRepository extends JpaRepository<AuditProfileLogEntry,Long> {

}
