package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.Client;
import org.javers.spring.annotation.JaversSpringDataAuditable;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Client entity.
 */
@SuppressWarnings("unused")
@JaversSpringDataAuditable
public interface ClientRepository extends JpaRepository<Client,Long> {

}
