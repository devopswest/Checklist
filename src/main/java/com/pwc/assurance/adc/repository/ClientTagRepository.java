package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.ClientTag;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ClientTag entity.
 */
@SuppressWarnings("unused")
public interface ClientTagRepository extends JpaRepository<ClientTag,Long> {

}
