package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.ChecklistAnswer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChecklistAnswer entity.
 */
@SuppressWarnings("unused")
public interface ChecklistAnswerRepository extends JpaRepository<ChecklistAnswer,Long> {

}
