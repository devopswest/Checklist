package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.License;
import org.javers.spring.annotation.JaversSpringDataAuditable;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the License entity.
 */
@SuppressWarnings("unused")
@JaversSpringDataAuditable
public interface LicenseRepository extends JpaRepository<License,Long> {

}
