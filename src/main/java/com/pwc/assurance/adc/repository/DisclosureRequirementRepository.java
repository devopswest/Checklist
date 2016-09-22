package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.DisclosureRequirement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DisclosureRequirement entity.
 */
@SuppressWarnings("unused")
public interface DisclosureRequirementRepository extends JpaRepository<DisclosureRequirement,Long> {

}
