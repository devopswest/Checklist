package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.Taxonomy;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Taxonomy entity.
 */
@SuppressWarnings("unused")
public interface TaxonomyRepository extends JpaRepository<Taxonomy,Long> {

}
