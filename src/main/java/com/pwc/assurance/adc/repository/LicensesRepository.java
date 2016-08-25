package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.Licenses;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Licenses entity.
 */
@SuppressWarnings("unused")
public interface LicensesRepository extends JpaRepository<Licenses,Long> {

}
