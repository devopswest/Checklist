package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.ClientLicense;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ClientLicense entity.
 */
@SuppressWarnings("unused")
public interface ClientLicenseRepository extends JpaRepository<ClientLicense,Long> {

}
