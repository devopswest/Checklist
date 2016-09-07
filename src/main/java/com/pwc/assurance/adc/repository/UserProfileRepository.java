package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.UserProfile;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserProfile entity.
 */
@SuppressWarnings("unused")
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

}
