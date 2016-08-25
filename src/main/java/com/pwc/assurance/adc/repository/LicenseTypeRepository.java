package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.LicenseType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LicenseType entity.
 */
@SuppressWarnings("unused")
public interface LicenseTypeRepository extends JpaRepository<LicenseType,Long> {

}
