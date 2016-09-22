package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.Taxonomy;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Taxonomy entity.
 */
@SuppressWarnings("unused")
public interface TaxonomyRepository extends JpaRepository<Taxonomy,Long> {
	
	@Query("select t from Taxonomy t where t.parent.code = :parentCode")
	public List<Taxonomy> getTaxonomiesByParent(@Param("parentCode") String parentCode);

}
