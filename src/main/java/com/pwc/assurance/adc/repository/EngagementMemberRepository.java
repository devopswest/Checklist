package com.pwc.assurance.adc.repository;

import com.pwc.assurance.adc.domain.EngagementMember;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EngagementMember entity.
 */
@SuppressWarnings("unused")
public interface EngagementMemberRepository extends JpaRepository<EngagementMember,Long> {

}
