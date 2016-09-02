package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.Checklist;
import org.javers.spring.annotation.JaversSpringDataAuditable;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Checklist entity.
 */
@SuppressWarnings("unused")
@JaversSpringDataAuditable
public interface ChecklistRepository extends JpaRepository<Checklist,Long> {

}
