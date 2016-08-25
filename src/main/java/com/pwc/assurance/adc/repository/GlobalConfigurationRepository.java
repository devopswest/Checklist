package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.GlobalConfiguration;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GlobalConfiguration entity.
 */
@SuppressWarnings("unused")
public interface GlobalConfigurationRepository extends JpaRepository<GlobalConfiguration,Long> {

}
