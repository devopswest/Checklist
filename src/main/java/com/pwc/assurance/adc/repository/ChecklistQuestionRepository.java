package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.ChecklistQuestion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChecklistQuestion entity.
 */
@SuppressWarnings("unused")
public interface ChecklistQuestionRepository extends JpaRepository<ChecklistQuestion,Long> {

}
