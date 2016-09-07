package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.FeatureAuthority;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FeatureAuthority entity.
 */
@SuppressWarnings("unused")
public interface FeatureAuthorityRepository extends JpaRepository<FeatureAuthority,Long> {

}
