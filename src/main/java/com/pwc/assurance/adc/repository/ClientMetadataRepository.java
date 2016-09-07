package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.ClientMetadata;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ClientMetadata entity.
 */
@SuppressWarnings("unused")
public interface ClientMetadataRepository extends JpaRepository<ClientMetadata,Long> {

}
