package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.Template;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Template entity.
 */
@SuppressWarnings("unused")
public interface TemplateRepository extends JpaRepository<Template,Long> {

}
