package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.ClientProfile;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ClientProfile entity.
 */
@SuppressWarnings("unused")
public interface ClientProfileRepository extends JpaRepository<ClientProfile,Long> {

}
