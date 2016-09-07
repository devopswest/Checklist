package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.Feature;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Feature entity.
 */
@SuppressWarnings("unused")
public interface FeatureRepository extends JpaRepository<Feature,Long> {

}
