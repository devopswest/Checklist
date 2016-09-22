package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.DisclosureRequirementTag;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DisclosureRequirementTag entity.
 */
@SuppressWarnings("unused")
public interface DisclosureRequirementTagRepository extends JpaRepository<DisclosureRequirementTag,Long> {

}
